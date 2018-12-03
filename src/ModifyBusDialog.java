import java.util.Map;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ModifyBusDialog {
	
	Label userPromptLable, busSelectorLabel, capacityLabel, speedLabel, routeLabel, initialStopLabel, 
			currentCapacityLabel, currentSpeedLabel, currentRouteLabel, currentInitialStopLabel,
			break1, break2, break3;
	TextField capacityTextField, speedTextField;
	ComboBox<String> busSelectorComboBox, routeComboBox, initialStopComboBox;	
	
	Button okayButton;
	Button cancelButton;
	
	Bus currentBus = null;
	Route currentRoute = null;
	Stop initialStop = null;
	
	
	public ModifyBusDialog(){
	}
	
	public void display(Simulation sim) {
		Stage window = new Stage();
		
		// Block event to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Modify Bus");
		
		// Make grid for user input
		GridPane rootCtr = new GridPane();
		rootCtr.setPadding(new Insets(10));
		rootCtr.setHgap(25);
		rootCtr.setVgap(5);
		
		int row = 0;
		
		// User Prompt
		userPromptLable = new Label("");
		GridPane.setHalignment(userPromptLable, HPos.CENTER);
		// col, row; span 5 cols and 1 row
		rootCtr.add(userPromptLable, 0, row++,  3, 1);
		
		// Bus Selector
		busSelectorLabel = new Label("Bus:");
		busSelectorComboBox = new ComboBox<String>();
		GridPane.setHalignment(busSelectorLabel, HPos.LEFT);
		GridPane.setHalignment(busSelectorComboBox, HPos.LEFT);
		rootCtr.add(busSelectorLabel, 0, row);
		rootCtr.add(busSelectorComboBox, 1, row++);
		
		busSelectorComboBox.getItems().add("");
		for (Map.Entry<Integer, Bus> item : sim.buses.entrySet()) {
		    Integer key = item.getKey();
		    Bus aBus = item.getValue();
		    
		    busSelectorComboBox.getItems().add(Integer.toString(aBus.getId()));
		}
		
		busSelectorComboBox.setOnAction(e -> {
//			System.out.println("Bus selection changed");
			
			if(! busSelectorComboBox.getValue().isEmpty()){
				// get current bus information
				currentBus = null;
				for (Map.Entry<Integer, Bus> item : sim.buses.entrySet()) {
				    Integer key = item.getKey();
				    Bus aBus = item.getValue();
				    
				    if(busSelectorComboBox.getValue().equalsIgnoreCase(Integer.toString(aBus.getId()))) {
						currentBus = aBus;
					}
				}
				
				if(currentBus != null) {
					// get current route info
					currentRoute = null;
					for (Map.Entry<Integer, Route> item : sim.routes.entrySet()) {
					    Integer key = item.getKey();
					    Route aRoute = item.getValue();
					    
					    if(currentBus.getRouteId() == aRoute.getId()) {
							currentRoute = aRoute;
						}
					}
					
					// Populate the initial current values
					currentCapacityLabel.setText("current: " + currentBus.getCapacity());
					currentSpeedLabel.setText("current: " + currentBus.getSpeed());
					currentRouteLabel.setText("current: " + currentRoute.getId() + " -- " + currentRoute.getName());
					initialStop = currentRoute.getStops().get(currentBus.getCurrentStopIndex());
					currentInitialStopLabel.setText("current: " + initialStop.getId() + " -- " + initialStop.getName());
				}
			}
			
			validate();
		});
		
		// HR
		break1 = new Label("Change Bus Capacity");
		break1.setStyle("-fx-font-weight: bold");
		GridPane.setHalignment(break1, HPos.LEFT);
		rootCtr.add(break1, 0, row++,  3, 1);
		
		// Bus Capacity
		capacityLabel = new Label("Max. Capacity:");
		capacityTextField = new TextField();
		currentCapacityLabel = new Label("current: ?");
		GridPane.setHalignment(capacityLabel, HPos.LEFT);
		GridPane.setHalignment(capacityTextField, HPos.LEFT);
		GridPane.setHalignment(currentCapacityLabel, HPos.LEFT);
		rootCtr.add(capacityLabel, 0, row);
		rootCtr.add(capacityTextField, 1, row++);
		rootCtr.add(currentCapacityLabel, 1, row++,  3, 1);
		
		capacityTextField.textProperty().addListener(e -> {
			validate();
		});
		
		// HR
		break2 = new Label("Change Bus Speed");
		break2.setStyle("-fx-font-weight: bold");
		GridPane.setHalignment(break2, HPos.LEFT);
		rootCtr.add(break2, 0, row++,  3, 1);
		
		// Bus Speed
		speedLabel = new Label("Speed (mph):");
		speedTextField = new TextField();
		currentSpeedLabel = new Label("current: ?");
		GridPane.setHalignment(speedLabel, HPos.LEFT);
		GridPane.setHalignment(speedTextField, HPos.LEFT);
		GridPane.setHalignment(currentSpeedLabel, HPos.LEFT);
		rootCtr.add(speedLabel, 0, row);
		rootCtr.add(speedTextField, 1, row++);
		rootCtr.add(currentSpeedLabel, 1, row++,  3, 1);
		
		speedTextField.textProperty().addListener(e -> {
			validate();
		});
		
		// HR
		break3 = new Label("Change Bus Route");
		break3.setStyle("-fx-font-weight: bold");
		GridPane.setHalignment(break3, HPos.LEFT);
		rootCtr.add(break3, 0, row++,  3, 1);

		// Bus Route Selector
		routeLabel = new Label("Route:");
		routeComboBox = new ComboBox<String>();
		currentRouteLabel = new Label("current: ?");
		GridPane.setHalignment(routeLabel, HPos.LEFT);
		GridPane.setHalignment(routeComboBox, HPos.LEFT);
		GridPane.setHalignment(currentRouteLabel, HPos.LEFT);
		rootCtr.add(routeLabel, 0, row);
		rootCtr.add(routeComboBox, 1, row++);
		rootCtr.add(currentRouteLabel, 1, row++,  3, 1);
		
		routeComboBox.getItems().add("");
		for (Map.Entry<Integer, Route> item : sim.routes.entrySet()) {
		    Integer key = item.getKey();
		    Route aRoute = item.getValue();
		    
		    String temp = aRoute.getId() + " -- " + aRoute.getName();
			routeComboBox.getItems().add(temp);
		}
		
			routeComboBox.setOnAction(e -> {
//			System.out.println("Route selection changed");
			
			if(routeComboBox.getValue() != null && ! routeComboBox.getValue().isEmpty()){
				// get current route info
				Route aRoute = null;
				for (Map.Entry<Integer, Route> item : sim.routes.entrySet()) {
				    Integer key = item.getKey();
				    Route thisRoute = item.getValue();
				    
				    if(Integer.parseInt(routeComboBox.getValue().split(" -- ")[0]) == thisRoute.getId()) {
						aRoute = thisRoute;
					}
				}
				
				initialStopComboBox.getSelectionModel().clearSelection();
				initialStopComboBox.getItems().clear();
				
				initialStopComboBox.getItems().add("");
				for(Stop aStop : aRoute.getStops()) {
					String temp = aStop.getId() + " -- " + aStop.getName();
					initialStopComboBox.getItems().add(temp);
		        }
			}
			
			validate();
		});		
		
		// Bus Route Initial Destination (Stop) Selector
		initialStopLabel = new Label("Initial Stop:");
		initialStopComboBox = new ComboBox<String>();
		currentInitialStopLabel = new Label("current: ?");
		GridPane.setHalignment(initialStopLabel, HPos.LEFT);
		GridPane.setHalignment(initialStopComboBox, HPos.LEFT);
		GridPane.setHalignment(currentInitialStopLabel, HPos.LEFT);
		rootCtr.add(initialStopLabel, 0, row);
		rootCtr.add(initialStopComboBox, 1, row++);
		rootCtr.add(currentInitialStopLabel, 1, row++,  3, 1);
		
		
		
		// Submission Buttons
		HBox rootCtr_buttonCtr = new HBox();
		GridPane.setHalignment(rootCtr_buttonCtr, HPos.LEFT);
		rootCtr.add(rootCtr_buttonCtr, 0, row++, 3, 1);
		
		okayButton = new Button("Update");
		cancelButton = new Button("Cancel");
		rootCtr_buttonCtr.getChildren().add(cancelButton);
		rootCtr_buttonCtr.getChildren().add(okayButton);
		
		okayButton.setOnAction(e -> {
//			System.out.println("Okay Button pressed");
			validate();
			
			boolean hasSuccess = false;
			
			// detect differing values
			if(!capacityTextField.getText().isEmpty()) {
				// has data
				//TODO call sim
				
				Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Bus Modification");
                alert.setHeaderText("Capacity change successful.");
                alert.setContentText("");
                alert.show();
                hasSuccess = true;
                
				capacityTextField.setText("");
			}
			
			if(!speedTextField.getText().isEmpty()) {
				// has data
				//TODO: call sim
				
				Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Bus Modification");
                alert.setHeaderText("Speed change successful.");
                alert.setContentText("");
                alert.show();
                hasSuccess = true;
				
				speedTextField.setText("");
			}
			
			if(!routeComboBox.getSelectionModel().isEmpty()
					&& !routeComboBox.getSelectionModel().getSelectedItem().toString().equals("")) {
				if(!initialStopComboBox.getSelectionModel().isEmpty()
						&& !initialStopComboBox.getSelectionModel().getSelectedItem().toString().equals("")) {
					// has data
					//TODO: call sim
					
					Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Bus Modification");
	                alert.setHeaderText("Route change successful.");
	                alert.setContentText("");
	                alert.show();
	                hasSuccess = true;
					
					routeComboBox.getSelectionModel().clearSelection();
					initialStopComboBox.getSelectionModel().clearSelection();
				} else {
					// missing second part
					userPromptLable.setText("Specify an initial stop.");
				}
			}
			
			
			// if everything is clear, we can close the dialog
			if(hasSuccess
					&& capacityTextField.getText().isEmpty()
					&& speedTextField.getText().isEmpty()
					&& routeComboBox.getValue() == null
					&& initialStopComboBox.getValue() == null
					) {
				window.close();
			}
			
			
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
		initialStopLabel.setVisible(false);
		currentInitialStopLabel.setVisible(false);
		// input
		capacityTextField.setVisible(false);
		speedTextField.setVisible(false);
		routeComboBox.setVisible(false);
		initialStopComboBox.setVisible(false);
		// breaks
		break1.setVisible(false);
		break2.setVisible(false);
		break3.setVisible(false);
		// buttons
		okayButton.setVisible(false);
		
		
		// Display window and wiat for it to be closed
		Scene scene = new Scene(rootCtr);
		window.setScene(scene);
		window.setResizable(false);
		window.showAndWait();
	}
	
	private boolean validate() {
		if(busSelectorComboBox.getValue().isEmpty()) {
			// labels
			capacityLabel.setVisible(false);
			currentCapacityLabel.setVisible(false);
			speedLabel.setVisible(false);
			currentSpeedLabel.setVisible(false);
			routeLabel.setVisible(false);
			currentRouteLabel.setVisible(false);
			initialStopLabel.setVisible(false);
			currentInitialStopLabel.setVisible(false);
			// input
			capacityTextField.setVisible(false);
			speedTextField.setVisible(false);
			routeComboBox.setVisible(false);
			initialStopComboBox.setVisible(false);
			// breaks
			break1.setVisible(false);
			break2.setVisible(false);
			break3.setVisible(false);
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
			initialStopLabel.setVisible(true);
			currentInitialStopLabel.setVisible(true);
			// input
			capacityTextField.setVisible(true);
			speedTextField.setVisible(true);
			routeComboBox.setVisible(true);
			initialStopComboBox.setVisible(true);
			// breaks
			break1.setVisible(true);
			break2.setVisible(true);
			break3.setVisible(true);
			// buttons
			okayButton.setVisible(true);
			
			userPromptLable.setText("");
			
			// detect differing values
			if(!capacityTextField.getText().isEmpty()) {
				// has data
				if (!capacityTextField.getText().matches("\\d*")) {
					capacityTextField.setText(capacityTextField.getText().replaceAll("[^\\d]", ""));
		        }
				if(capacityTextField.getText().length() > 0
						&& Integer.parseInt(capacityTextField.getText()) < 0) {
					userPromptLable.setText("Capacity must be greater than -1.");
					return false;
				}
			}
			
			if(!speedTextField.getText().isEmpty()) {
				// has data
				if (!speedTextField.getText().matches("\\d*")) {
					speedTextField.setText(speedTextField.getText().replaceAll("[^\\d]", ""));
		        }
				if(speedTextField.getText().length() > 0
						&& Integer.parseInt(speedTextField.getText()) < 1) {
					userPromptLable.setText("Bus speed must be greater than 0.");
					return false;
				}
					
			}
			
			if(routeComboBox.getSelectionModel().getSelectedItem() != null 
					&& !routeComboBox.getSelectionModel().getSelectedItem().toString().equals("")) {
				if(initialStopComboBox.getSelectionModel().getSelectedItem() != null 
						&& !initialStopComboBox.getSelectionModel().getSelectedItem().toString().equals("")) {
					// has data
				} else {
					userPromptLable.setText("Specify an initial stop.");
					return false;
				}
			}
		}
		return true;
	}
}
