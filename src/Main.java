import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	Simulation sim;
	private Stage window;
	Canvas busMapCanvas;
	private Button button;
	
	public static void main(String[] args) 
	{
		launch(args);
	}

    @Override
    public void start(Stage primaryStage) {
    	
    	// start out simulation with the args
    	final List<String> params = getParameters().getRaw();
    	sim = new Simulation(params.get(0),20);
    	
    	
    	window = primaryStage;
        
        button = new Button();
        button.setText("Hello world");
        // could be handled with lamda
        // button.setOnAction(e -> code);
        button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("you made it");
			}
        }); // alternative e -> code
                
        Group root = new Group();
        busMapCanvas = new Canvas(600, 600);
        GraphicsContext gc = busMapCanvas.getGraphicsContext2D();
        drawStops(gc);
        root.getChildren().add(busMapCanvas);
        
        Scene mainScene = new Scene(root);
        
        window.setTitle("Mass Transit Simulator - Team 47");
        window.setScene(mainScene);
        window.show();
    }

    private void drawStops(GraphicsContext gc) {
        
        // Determine our graphics range 
        double[] longRange = {0, 0};
        double[] latRange = {0, 0};
        for(Stop aStop:sim.stops) {
        	longRange[0] = Math.min(aStop.longitude, longRange[0]);
        	longRange[1] = Math.max(aStop.longitude, longRange[1]);
        	latRange[0] = Math.min(aStop.latitude, latRange[0]);
        	latRange[1] = Math.max(aStop.latitude, latRange[1]);
        }
        
        double longSize = longRange[1] - longRange[0]; 
        double latSize = latRange[1] - latRange[0]; 
        
        //TODO: add buffer for the boarder
        //TODO: multiple by 2 to span the entire window
        
        double longRatio = Math.round(busMapCanvas.getWidth() * .50) / longSize;
        double latRatio = Math.round(busMapCanvas.getHeight() * .50) / latSize;
        
        double longOffset = longSize - Math.max(Math.abs(longRange[0]), longRange[1]);
        double latOffset = latSize - Math.max(Math.abs(latRange[0]), latRange[1]);

        
    	System.out.println("Stops longitude\n    range: " + longRange[0] 
        		+ ", " + longRange[1] + "\n    size:" + longSize 
        		+ "\n    ratio:" + longRatio);
        System.out.println("Stops latitude\n    range: " + latRange[0] 
        		+ ", " + latRange[1] + "\n    size:" + latSize 
        		+ "\n    ratio:" + latRatio);
        System.out.println("Axes Sizes\n    long: " + longSize 
        		+ "\n    lat:" + latSize);
        System.out.println("Offsets\n    long: " + longOffset 
        		+ "\n    lat:" + latOffset);
        
        //TODO: draw buffer
        
        
        
        // for each stop
        for(Stop aStop : sim.stops) {
        	int width = 10;
        	int height = 10;
        	// longitude = x-axis
        	int x = (int) Math.round(longRatio * (aStop.longitude
        			+ longOffset)) * 2;
 
        	// latitude = y-axis
        	int y = (int) Math.round(latRatio * (aStop.latitude
        			+ latOffset)) * 2;
       
        	
        	
        	System.out.println("Stop id:" + aStop.id 
        			+ " long:" + aStop.longitude
        			+ " x:" + x
        			+ " lat:" + aStop.latitude        			
        			+ " y:" + y
        			);
        	
        	// x, y, w, h, archW, archH
        	gc.setStroke(Color.BLACK);
        	gc.setLineWidth(1);
        	gc.strokeRoundRect(
        			x - (width * .5),
        			y - (height * .5), 
        			width, 
        			height, 
        			0, // corner archWidth
        			0); // corner archHeight
        	gc.setStroke(Color.BLUE);
        	gc.setLineWidth(1);
        	gc.strokeText("Stop #" + aStop.id, x, y);

        }
        
    }

}
