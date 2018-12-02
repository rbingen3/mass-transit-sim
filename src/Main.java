import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	private Simulation sim;
	private Stage window;
	private Canvas busCanvas;
	private GraphicsContext busCanvasGraphicsContext;
	private Button previousButton, nextButton;
	
	String busCharString = "\uD83D\uDE8C";
	String busStopCharString = "\uD83D\uDE8F";
	
	int cycleCount = 0;
	
	public static void main(String[] args) {
		if(args.length < 1){
			System.out.println("Configuration file not specified, exiting simulation.");
			System.exit(0);
		}
		launch(args);
	}

	Label cycleMsg1, cycleMsg2, cycleMsg3;
    @Override
    public void start(Stage primaryStage) {
    	// Remember:
    	//  Latitude (east to west) <==> y-axis (-90 to 90) <==> Height
    	// 	Longitude (north to south) <==> x-axis (-180 to 180) <==> Width
    	//  See https://gisgeography.com/latitude-longitude-coordinates/
    	
    	// In our sim, we max Latitude and Longitude to -1 and 1
    	
    	// get our simulation args
    	final List<String> params = getParameters().getRaw();
    	
    	// launch the simulation
    	//TODO: consider remove iteration count
    	sim = new Simulation(params.get(0), 0);
    	
    	// set the primary stage
    	window = primaryStage;
        
    	// create root container
    	VBox rootCtr = new VBox();
    	rootCtr.setSpacing(10);
    	rootCtr.setPadding(new Insets(10, 10, 10, 10)); // bottom, left, right, top
    	
	    	// rootCtr, create row 1 container
    		// for canvas area
	    	busCanvas = new Canvas(1200, 800); // width, height
	        busCanvasGraphicsContext = busCanvas.getGraphicsContext2D();
	        redraw(busCanvasGraphicsContext);
	        rootCtr.getChildren().add(busCanvas);
	    	
	    	// rootCtr, create row 2 container
	        // for HBox
	        HBox rootCtr_row2Ctr = new HBox();
	        rootCtr_row2Ctr.setSpacing(10);
	        rootCtr_row2Ctr.setPadding(new Insets(10, 10, 10, 10));
	        rootCtr_row2Ctr.setAlignment(Pos.CENTER); // align contents
	        rootCtr_row2Ctr.setFillHeight(true); //vertically align contents
	        rootCtr.getChildren().add(rootCtr_row2Ctr);
		        
		        // rootCtr_row2Ctr, create col 1 contents
	        	//TODO: Disable the previousButton if unable to go back
		        previousButton = new Button();
		        previousButton.setText("\u2190 Preform Previous Cycle");
		        previousButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
//						System.out.println("Previous button clicked");
						if(cycleCount > 0) {
						// Perform update
						sim.rewindSimulation();
						cycleCount--;
						// Update displays
						redraw(busCanvasGraphicsContext);
						} else {
							System.out.println("No previous cycles exists.");
						}
					}
		        }); // alternative (e -> code)
		        rootCtr_row2Ctr.getChildren().add(previousButton);
		        
		        // rootCtr_row2Ctr, create col 2 container
		        VBox rootCtr_row2Ctr_col2Ctr = new VBox();
		        rootCtr_row2Ctr_col2Ctr.setSpacing(0);
		        rootCtr_row2Ctr_col2Ctr.setPadding(new Insets(0, 10, 10, 0));
		        rootCtr_row2Ctr_col2Ctr.setAlignment(Pos.CENTER);
		        rootCtr_row2Ctr.getChildren().add(rootCtr_row2Ctr_col2Ctr);
		        
		        	// rootCtr_row2Ctr_col2Ctr create col 1 contents
		        	cycleMsg1 = new Label("Line1");
		        	rootCtr_row2Ctr_col2Ctr.getChildren().add(cycleMsg1);
		        	
		        	// rootCtr_row2Ctr_col2Ctr create col 2 contents
		        	cycleMsg2 = new Label("Line2 sdgsdfg");
		        	rootCtr_row2Ctr_col2Ctr.getChildren().add(cycleMsg2);
		        	
		        	// rootCtr_row2Ctr_col2Ctr create col 3 contents
		        	cycleMsg3 = new Label("Line3");
		        	rootCtr_row2Ctr_col2Ctr.getChildren().add(cycleMsg3);
		        
		        
		        // rootCtr_row2Ctr, create col 3 contents
		        nextButton = new Button();
		        nextButton.setText("Perform Next Cycle \u2192");
		        nextButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
//						System.out.println("Next button clicked");
						// Perform update
						sim.runSimulation(1);
						cycleCount++;
						// Update displays
						redraw(busCanvasGraphicsContext);
					}
		        }); // alternative (e -> code)
		        rootCtr_row2Ctr.getChildren().add(nextButton);
        
        
        
        //TODO: create listener for adjusting the canvas
        Scene mainScene = new Scene(rootCtr);
        window.setTitle("Mass Transit Simulator - Team 47");
        window.setResizable(false);
        window.setScene(mainScene);
        window.show();
        redraw(busCanvasGraphicsContext);
    } // end start()
    
    
    private double[] longRange = {0, 0};
    private double[] latRange = {0, 0};
    double longSize, latSize;
    double BUFFER_PERCENT = 0.22;
    double longBuffer, latBuffer;
    double longRatio, latRatio;
    double longOffset, latOffset;
    private void redraw(GraphicsContext gc) {
    	
    	// fill in background
    	gc.setFill(Color.SILVER);
    	gc.fillRect(0, 0, busCanvas.getWidth(), busCanvas.getHeight());
        
        // Determine our graphics range 
        longRange = new double[2];
        latRange = new double[2];
        for(Stop aStop : sim.stops) {
        	longRange[0] = Math.min(aStop.getLongitude(), longRange[0]);
        	longRange[1] = Math.max(aStop.getLongitude(), longRange[1]);
        	latRange[0] = Math.min(aStop.getLatitude(), latRange[0]);
        	latRange[1] = Math.max(aStop.getLatitude(), latRange[1]);
        }
        for(Depot aDepot : sim.depots) {
        	longRange[0] = Math.min(aDepot.getLongitude(), longRange[0]);
        	longRange[1] = Math.max(aDepot.getLongitude(), longRange[1]);
        	latRange[0] = Math.min(aDepot.getLatitude(), latRange[0]);
        	latRange[1] = Math.max(aDepot.getLatitude(), latRange[1]);
        }
        longSize = longRange[1] - longRange[0]; 
        latSize = latRange[1] - latRange[0];
        
        // Compute the buffer and offset
        longBuffer = busCanvas.getWidth() * BUFFER_PERCENT * .5;
        latBuffer = busCanvas.getHeight() * BUFFER_PERCENT * .5;
        
        // Determine the lat/long ratios
        longRatio = Math.round((busCanvas.getWidth() - longBuffer * 2) * .50) 
        		/ longSize;
        latRatio = Math.round((busCanvas.getHeight() - latBuffer *2) * .50) 
        		/ latSize;
        
        // Compute the offset for negative values
        longOffset = longSize - Math.max(Math.abs(longRange[0]), longRange[1]);
        latOffset = latSize - Math.max(Math.abs(latRange[0]), latRange[1]);
        
        
//    	System.out.println("Stops longitude\n    range: " + longRange[0] 
//        		+ ", " + longRange[1] + "\n    size:" + longSize 
//        		+ "\n    ratio:" + longRatio);
//        System.out.println("Stops latitude\n    range: " + latRange[0] 
//        		+ ", " + latRange[1] + "\n    size:" + latSize 
//        		+ "\n    ratio:" + latRatio);
//        System.out.println("Axes Sizes\n    long: " + longSize 
//        		+ "\n    lat:" + latSize);
//        System.out.println("Offsets\n    long: " + longOffset 
//        		+ "\n    lat:" + latOffset);
        
        
        // for each stop
        for(Stop aStop : sim.stops) {
        	drawStop(aStop, gc);
        }
        for(Depot aDepot : sim.depots) {
        	drawStop(aDepot, gc);
        }
        
        if(cycleMsg1 != null)
        	cycleMsg1.setText("Time: " + Integer.toString(sim.currentTime));
        if(cycleMsg2 != null)
        	cycleMsg2.setText("Cycle: " + Integer.toString(cycleCount));
        //TODO: complete effecency
        if(cycleMsg3 != null)
        	cycleMsg3.setText("Effencency: ");
        
        
    } // end drawStop()
    
    private void drawStop(Stop aStop, GraphicsContext gc) {
    	// must be square
    	// TODO: fix square constraint
    	int WIDTH = 40;
    	int HEIGHT = 40;
    	int MAX_BUS_IMAGE_WIDTH = 40;
    	int MAX_BUS_IMAGE_HEIGHT = 40;
    	
    	// longitude = x-axis
    	int centerX = (int) Math.round(longRatio * (aStop.getLongitude()
    			+ longOffset) * 2 + longBuffer);
    	// latitude = y-axis
    	// Because the canvas' origin (0, 0) is (top, left) 
    	// and not (bottom, left) as with most math...
    	// we must start with the total height
    	int centerY = (int) Math.round(busCanvas.getHeight() - (latRatio * (aStop.getLatitude()
    			+ latOffset) * 2 + latBuffer));
    	
//    	System.out.println("Stop id:" + aStop.getId()
//    			+ " long:" + aStop.getLongitude()
//    			+ " x:" + centerX
//    			+ " lat:" + aStop.getLatitude()        			
//    			+ " y:" + centerY
//    			);
    	
    	// -- Compute stops
    	// for each bus
    	String stopBusesDisplayString = "";
        for(Bus aBus : sim.buses) {
        	// Determine if the bus is at the stop
        	
        	// Get the current route the bus is on
        	Route aRoute = null;
        	for(Route thisRoute : sim.routes) {
        		if(thisRoute.getId() == aBus.getRouteId()) {
        			aRoute = thisRoute;
        		}
        	}
        	if(aRoute == null) {
        		System.out.println("No route found for bus id " + aBus.getId());
        	}
        	
        	// Get the current stop the bus is on
        	int stopId = aRoute.getStops().get(aBus.getCurrentStopIndex()).getId();
        	
        	// Display the bus info is at the current stop
        	if(stopId == aStop.getId()) {
        		if(stopBusesDisplayString.length() > 0) {
        			stopBusesDisplayString += "\n";
        		}
        		stopBusesDisplayString += aBus.getDisplayString();
        	}
        }    	
    	
    	
    	// -- Display bus or depot sign
    	Image image = null;
    	if(aStop.getClass().getSimpleName().equalsIgnoreCase("stop")){
    		image = new Image("file:res/BusStop.png");
    	} else if(aStop.getClass().getSimpleName().equalsIgnoreCase("depot")){
    		image = new Image("file:res/BusDepot.png");
    	}
    	// width = 240
    	// height = 179
    	// devisor 6
    	double imageWidth = image.getWidth();
    	double imageHeight = image.getHeight();
    	double imageBase = Math.max(imageWidth, imageHeight);
    	imageWidth /= imageBase;
    	imageHeight /= imageBase;
    	imageWidth *= WIDTH;
    	imageHeight *= HEIGHT;
//    	System.out.println("image w:" + imageWidth + ", h:" + imageHeight);
    	busCanvasGraphicsContext.drawImage(image, 
    			centerX - (int) Math.round(imageWidth * 0.5),
    			centerY - (int) Math.round(imageHeight * 0.5), 
    			imageWidth, 
    			imageHeight);
    	

    	// -- generate bus photo if a bus is there
    	if(stopBusesDisplayString != null && !stopBusesDisplayString.isEmpty()) {
        	Image busImage = new Image("file:res/CityBus.gif");
        	double busImageWidth = busImage.getWidth();
        	double busImageHeight = busImage.getHeight();
        	double busImageBase = Math.max(busImageWidth, busImageHeight);
        	busImageWidth /= busImageBase;
        	busImageHeight /= busImageBase;
        	busImageWidth *= MAX_BUS_IMAGE_WIDTH;
        	busImageHeight *= MAX_BUS_IMAGE_HEIGHT;
//        	System.out.println("image w:" + imageWidth + ", h:" + imageHeight);
        	
        	// -- Generate text
        	busCanvasGraphicsContext.drawImage(busImage, 
        			centerX  + 36 - (int) Math.round(busImageWidth * 0.5),
        			centerY + 6 - (int) Math.round(busImageHeight * 0.5), 
        			busImageWidth, 
        			busImageHeight);   
    	}
    	
    	// -- Generate text
    	
    	// print name above icon
    	gc.setFont(new javafx.scene.text.Font("Arial", 12));
    	busCanvasGraphicsContext.setFill(Color.BLUE);
    	busCanvasGraphicsContext.fillText(aStop.getName(), 
    			centerX - (WIDTH / 2.0), centerY - (HEIGHT / 2.0));
    	
    	// print stop info to right of name
    	gc.setFont(new javafx.scene.text.Font("Arial", 12));
    	busCanvasGraphicsContext.setStroke(Color.GRAY);
    	busCanvasGraphicsContext.strokeText("(" + aStop.getClass().getSimpleName().toUpperCase()
    			+ " #" + aStop.getId() + ")" , 
    			centerX + (WIDTH / 2.0), centerY - 6);
    	
    	gc.setFont(new javafx.scene.text.Font("Arial", 10));
    	busCanvasGraphicsContext.setFill(Color.BLACK);
    	busCanvasGraphicsContext.fillText(stopBusesDisplayString , 
    			centerX - (WIDTH / 2.0), centerY + 26);
    	
    }

}
