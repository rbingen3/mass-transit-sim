import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ModifyBusDialog {
	
	Label userPromptLable, busSelectorLabel, capacityLabel, speedLabel, routeLabel, 
			currentCapacityLabel, currentSpeedLabel, currentRouteLabel;
	TextField capacityTextField, speedTextField;
	ComboBox<String> busSelectorComboBox, routeComboBox;	
	
	Button okayButton;
	Button cancelButton;
	
	public ModifyBusDialog(){
	}
	
	public void display(Simulation sim) {
		Stage window = new Stage();
		
		// Block event to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Modify Bus");
		
		// Make grid for user input
		GridPane rootCtr = new GridPane();
		rootCtr.setPadding(new Insets(20));
		rootCtr.setHgap(25);
		rootCtr.setVgap(15);
		
		int row = 0;
		
		// User Prompt
		userPromptLable = new Label("");
		GridPane.setHalignment(userPromptLable, HPos.CENTER);
		// col, row; span 5 cols and 1 row
		rootCtr.add(userPromptLable, 0, row++,  5, 1);
		
		// Bus Selector
		busSelectorLabel = new Label("Bus:");
		busSelectorComboBox = new ComboBox<String>();
		GridPane.setHalignment(busSelectorLabel, HPos.LEFT);
		GridPane.setHalignment(busSelectorComboBox, HPos.LEFT);
		rootCtr.add(busSelectorLabel, 0, row);
		rootCtr.add(busSelectorComboBox, 1, row++);
		
		busSelectorComboBox.getItems().add("");
		for(Bus aBus : sim.buses) {
			busSelectorComboBox.getItems().add(Integer.toString(aBus.getId()));
        }
		
		busSelectorComboBox.setOnAction(e -> {
			System.out.println("Bus selection changed");
			// get values
			
			
			// update routes for selected bug
			routeComboBox.getItems().add("");
			for(Bus aRoute : sim.buses) {
				busSelectorComboBox.getItems().add(Integer.toString(aRoute.getId()));
	        }
			
			validate();
		});
		
		// Bus Capacity
		capacityLabel = new Label("Max. Capacity:");
		capacityTextField = new TextField();
		currentCapacityLabel = new Label("current: ?");
		GridPane.setHalignment(capacityLabel, HPos.LEFT);
		GridPane.setHalignment(capacityTextField, HPos.LEFT);
		GridPane.setHalignment(currentCapacityLabel, HPos.LEFT);
		rootCtr.add(capacityLabel, 0, row);
		rootCtr.add(capacityTextField, 1, row);
		rootCtr.add(currentCapacityLabel, 2, row++);
		
		// Bus Speed
		speedLabel = new Label("Speed (mph):");
		speedTextField = new TextField();
		currentSpeedLabel = new Label("current: ?");
		GridPane.setHalignment(speedLabel, HPos.LEFT);
		GridPane.setHalignment(speedTextField, HPos.LEFT);
		GridPane.setHalignment(currentSpeedLabel, HPos.LEFT);
		rootCtr.add(speedLabel, 0, row);
		rootCtr.add(speedTextField, 1, row);
		rootCtr.add(currentSpeedLabel, 2, row++);
		
		// Bus Route Selector
		routeLabel = new Label("Route:");
		routeComboBox = new ComboBox<String>();
		currentRouteLabel = new Label("current: ?");
		GridPane.setHalignment(routeLabel, HPos.LEFT);
		GridPane.setHalignment(routeComboBox, HPos.LEFT);
		GridPane.setHalignment(currentRouteLabel, HPos.LEFT);
		rootCtr.add(routeLabel, 0, row);
		rootCtr.add(routeComboBox, 1, row);
		rootCtr.add(currentRouteLabel, 2, row++);
		
		
		// Submission Buttons
		HBox rootCtr_buttonCtr = new HBox();
		GridPane.setHalignment(rootCtr_buttonCtr, HPos.LEFT);
		rootCtr.add(rootCtr_buttonCtr, 0, row++, 5, 1);
		
		okayButton = new Button("Update");
		cancelButton = new Button("Cancel");
		rootCtr_buttonCtr.getChildren().add(cancelButton);
		rootCtr_buttonCtr.getChildren().add(okayButton);
		
		okayButton.setOnAction(e -> {
//			System.out.println("Okay Button pressed");
			//TODO: validate
			//TODO: Process
		});
		
		cancelButton.setOnAction(e -> {
//			System.out.println("Cancel Button pressed");
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		});
		
		
		// Set Default Visible
		userPromptLable.setText("Select a bus from the drop down.");
		// labels
		capacityLabel.setVisible(false);
		currentCapacityLabel.setVisible(false);
		speedLabel.setVisible(false);
		currentSpeedLabel.setVisible(false);
		routeLabel.setVisible(false);
		currentRouteLabel.setVisible(false);
		// input
		capacityTextField.setVisible(false);
		speedTextField.setVisible(false);
		routeComboBox.setVisible(false);
		// buttons
		okayButton.setVisible(false);
		
		
		// Display window and wiat for it to be closed
		Scene scene = new Scene(rootCtr);
		window.setScene(scene);
		window.setResizable(false);
		window.showAndWait();
	}
	
	private void validate() {
		if(busSelectorComboBox.getValue().isEmpty()) {
			// labels
			capacityLabel.setVisible(false);
			currentCapacityLabel.setVisible(false);
			speedLabel.setVisible(false);
			currentSpeedLabel.setVisible(false);
			routeLabel.setVisible(false);
			currentRouteLabel.setVisible(false);
			// input
			capacityTextField.setVisible(false);
			speedTextField.setVisible(false);
			routeComboBox.setVisible(false);
			// buttons
			okayButton.setVisible(false);
			
			userPromptLable.setText("Select a bus from the drop down.");
		} else {
			// labels
			capacityLabel.setVisible(true);
			currentCapacityLabel.setVisible(true);
			speedLabel.setVisible(true);
			currentSpeedLabel.setVisible(true);
			routeLabel.setVisible(true);
			currentRouteLabel.setVisible(true);
			// input
			capacityTextField.setVisible(true);
			speedTextField.setVisible(true);
			routeComboBox.setVisible(true);
			// buttons
			okayButton.setVisible(true);
			
			//TODO: additional validation logic
		}
	}
}
