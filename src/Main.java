import java.awt.Font;
import java.io.File;
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
	
	public static void main(String[] args) {
		launch(args);
	}

	
    @Override
    public void start(Stage primaryStage) {
    	
    	// get our simulation args
    	final List<String> params = getParameters().getRaw();
    	
    	// launch the simulation
    	//TODO: remove iteration count
    	sim = new Simulation(params.get(0), 0);
    	
    	// set the primary stage
    	window = primaryStage;
        
    	// create root container
    	VBox rootCtr = new VBox();
    	rootCtr.setSpacing(10);
    	rootCtr.setPadding(new Insets(10, 10, 10, 10)); // bottom, left, right, top
    	
	    	// rootCtr, create row 1 container
    		// for canvas area
	    	busCanvas = new Canvas(1200, 800);
	        busCanvasGraphicsContext = busCanvas.getGraphicsContext2D();
	        drawStops(busCanvasGraphicsContext);
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
						System.out.println("Previous button clicked");
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
		        	Text cycleMsg1 = new Text("Line1");
		        	rootCtr_row2Ctr_col2Ctr.getChildren().add(cycleMsg1);
		        	
		        	// rootCtr_row2Ctr_col2Ctr create col 2 contents
		        	Text cycleMsg2 = new Text("Line2 sdgsdfg");
		        	rootCtr_row2Ctr_col2Ctr.getChildren().add(cycleMsg2);
		        	
		        	// rootCtr_row2Ctr_col2Ctr create col 3 contents
		        	Text cycleMsg3 = new Text("Line3");
		        	rootCtr_row2Ctr_col2Ctr.getChildren().add(cycleMsg3);
		        
		        
		        // rootCtr_row2Ctr, create col 3 contents
		        nextButton = new Button();
		        nextButton.setText("Perform Next Cycle \u2192");
		        nextButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Next button clicked");
						// Perform update
						
					}
		        }); // alternative (e -> code)
		        rootCtr_row2Ctr.getChildren().add(nextButton);
        
        
        
        //TODO: create listener for adjusting the canvas
        Scene mainScene = new Scene(rootCtr);
        window.setTitle("Mass Transit Simulator - Team 47");
        window.setResizable(false);
        window.setScene(mainScene);
        window.show();
    } // end start()
    
    
    private double[] longRange = {0, 0};
    private double[] latRange = {0, 0};
    double longSize, latSize;
    double BUFFER_PERCENT = 0.22;
    double longBuffer, latBuffer;
    double longRatio, latRatio;
    double longOffset, latOffset;
    private void drawStops(GraphicsContext gc) {
    	
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
        	drawStop(aStop);
        }
        for(Depot aDepot : sim.depots) {
        	drawStop(aDepot);
        }
    } // end drawStop()
    
    private void drawStop(Stop aStop) {
    	// must be square
    	int WIDTH = 40;
    	int HEIGHT = 40;
    	
    	// longitude = x-axis
    	int centerX = (int) Math.round(longRatio * (aStop.getLongitude()
    			+ longOffset) * 2 + longBuffer);
    	// latitude = y-axis
    	int centerY = (int) Math.round(latRatio * (aStop.getLatitude()
    			+ latOffset) * 2 + latBuffer);
    	
//    	System.out.println("Stop id:" + aStop.getId()
//    			+ " long:" + aStop.getLongitude()
//    			+ " x:" + centerX
//    			+ " lat:" + aStop.getLatitude()        			
//    			+ " y:" + centerY
//    			);
    	
    	
    	
    	Image image = null;
    	if(aStop.getClass().getSimpleName().equalsIgnoreCase("stop")){
    		image = new Image("file:res/BusStop.png");
    	} else if(aStop.getClass().getSimpleName().equalsIgnoreCase("depot")){
    		image = new Image("file:res/BusDepot_Red.png");
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
    	
    	busCanvasGraphicsContext.setLineWidth(1);
    	// print name above icon
    	busCanvasGraphicsContext.setStroke(Color.BLUE);
    	busCanvasGraphicsContext.strokeText(aStop.getName(), 
    			centerX - (WIDTH / 2.0), centerY - (HEIGHT / 2.0));
    	// print stop info to right of name
    	busCanvasGraphicsContext.setStroke(Color.GRAY);
    	busCanvasGraphicsContext.strokeText("(" + aStop.getClass().getSimpleName().toUpperCase()
    			+ " #" + aStop.getId() + ")" , 
    			centerX + (WIDTH / 2.0), centerY - 6);
    }

}
