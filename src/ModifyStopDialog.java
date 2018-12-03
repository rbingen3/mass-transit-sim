import java.util.List;
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


public class ModifyStopDialog {
	
	Label userPromptLable, stopSelectorLabel, ridersArrivalLabel, ridersOffLabel,
			ridersOnLabel, ridersDepartLabel, break1, high, low;
	TextField ridersArrivalHigh, ridersArrivalLow, ridersOffHigh, ridersOffLow, 
			ridersOnHigh, ridersOnLow, ridersDepartHigh, ridersDepartLow;
	Label currentRidersArrival, currentRidersOff, currentRidersOn, currentRidersDepart;
	ComboBox<String> stopSelectorComboBox;	
	
	Button okayButton;
	Button cancelButton;
	
	Stop currentStop = null;
	
	
	public ModifyStopDialog(){
	}
	
	public void display(Simulation sim) {
		Stage window = new Stage();
		
		// Block event to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Modify Stop");
		
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
		
		// Stop Selector
		stopSelectorLabel = new Label("Stop:");
		stopSelectorComboBox = new ComboBox<String>();
		GridPane.setHalignment(stopSelectorLabel, HPos.LEFT);
		GridPane.setHalignment(stopSelectorComboBox, HPos.LEFT);
		rootCtr.add(stopSelectorLabel, 0, row);
		rootCtr.add(stopSelectorComboBox, 1, row++, 2, 1);
		
		stopSelectorComboBox.getItems().add("");
		for (Map.Entry<Integer, Stop> item : sim.stops.entrySet()) {
		    Integer key = item.getKey();
		    Stop aStop = item.getValue();
		    
		    stopSelectorComboBox.getItems().add(Integer.toString(aStop.getId())
		    		+ " -- " + aStop.getName());
		}
		
		stopSelectorComboBox.setOnAction(e -> {
//			System.out.println("Bus selection changed");
			
			if(! stopSelectorComboBox.getValue().isEmpty()){
				// get current bus information
				currentStop = null;
				for (Map.Entry<Integer, Stop> item : sim.stops.entrySet()) {
				    Integer key = item.getKey();
				    Stop aStop = item.getValue();
				    
				    if(stopSelectorComboBox.getSelectionModel().getSelectedItem().toString().split(" -- ")[0]
				    		.equalsIgnoreCase(Integer.toString(aStop.getId()))) {
						currentStop = aStop;
					}
				}
				
				if(currentStop != null) {
					// get current values
					currentRidersArrival.setText("current: High " 
							+ currentStop.getRidersArriveHigh()
							+ ", Low " + currentStop.getRidersArriveLow());
					currentRidersOff.setText("current: High " 
							+ currentStop.getRidersOffHigh()
							+ ", Low " + currentStop.getRidersOffLow());
					currentRidersOn.setText("current: High " 
							+ currentStop.getRidersOnHigh()
							+ ", Low " + currentStop.getRidersOnLow());
					currentRidersDepart.setText("current: High " 
							+ currentStop.getRidersDepartHigh()
							+ ", Low " + currentStop.getRidersDepartLow());				
				}
			}
			
			validate();
		});
		
		// HR
		break1 = new Label("Change Rider Probability");
		break1.setStyle("-fx-font-weight: bold");
		GridPane.setHalignment(break1, HPos.LEFT);
		rootCtr.add(break1, 0, row++,  3, 1);
		
		
		// General Lable
		high = new Label("High");
		low = new Label("Low");
		GridPane.setHalignment(high, HPos.CENTER);
		GridPane.setHalignment(low, HPos.CENTER);
		rootCtr.add(high, 1, row);
		rootCtr.add(low, 2, row++);
		
		
		// Riders Arrival High, Low
		ridersArrivalLabel = new Label("Riders Arrive:");
		ridersArrivalHigh = new TextField();
		ridersArrivalLow = new TextField();
		currentRidersArrival = new Label("current: ?");
		GridPane.setHalignment(ridersArrivalLabel, HPos.LEFT);
		GridPane.setHalignment(ridersArrivalHigh, HPos.LEFT);
		GridPane.setHalignment(ridersArrivalLow, HPos.LEFT);
		GridPane.setHalignment(currentRidersArrival, HPos.LEFT);
		rootCtr.add(ridersArrivalLabel, 0, row);
		rootCtr.add(ridersArrivalHigh, 1, row);
		rootCtr.add(ridersArrivalLow, 2, row++);
		rootCtr.add(currentRidersArrival, 1, row++,  3, 1);
		
		ridersArrivalHigh.textProperty().addListener(e -> {
			validate();
		});
		ridersArrivalLow.textProperty().addListener(e -> {
			validate();
		});
		
		
		// Riders Off High, Low
		ridersOffLabel = new Label("Riders Off:");
		ridersOffHigh = new TextField();
		ridersOffLow = new TextField();
		currentRidersOff = new Label("current: ?");
		GridPane.setHalignment(ridersOffLabel, HPos.LEFT);
		GridPane.setHalignment(ridersOffHigh, HPos.LEFT);
		GridPane.setHalignment(ridersOffLow, HPos.LEFT);
		GridPane.setHalignment(currentRidersOff, HPos.LEFT);
		rootCtr.add(ridersOffLabel, 0, row);
		rootCtr.add(ridersOffHigh, 1, row);
		rootCtr.add(ridersOffLow, 2, row++);
		rootCtr.add(currentRidersOff, 1, row++,  3, 1);
		
		ridersOffHigh.textProperty().addListener(e -> {
			validate();
		});
		ridersOffLow.textProperty().addListener(e -> {
			validate();
		});
		
		
		// Riders On High, Low
		ridersOnLabel = new Label("Riders On:");
		ridersOnHigh = new TextField();
		ridersOnLow = new TextField();
		currentRidersOn = new Label("current: ?");
		GridPane.setHalignment(ridersOnLabel, HPos.LEFT);
		GridPane.setHalignment(ridersOnHigh, HPos.LEFT);
		GridPane.setHalignment(ridersOnLow, HPos.LEFT);
		GridPane.setHalignment(currentRidersOn, HPos.LEFT);
		rootCtr.add(ridersOnLabel, 0, row);
		rootCtr.add(ridersOnHigh, 1, row);
		rootCtr.add(ridersOnLow, 2, row++);
		rootCtr.add(currentRidersOn, 1, row++,  3, 1);
		
		ridersOnHigh.textProperty().addListener(e -> {
			validate();
		});
		ridersOnLow.textProperty().addListener(e -> {
			validate();
		});
		
		// Riders Depart High, Low
		ridersDepartLabel = new Label("Riders Depart:");
		ridersDepartHigh = new TextField();
		ridersDepartLow = new TextField();
		currentRidersDepart = new Label("current: ?");
		GridPane.setHalignment(ridersDepartLabel, HPos.LEFT);
		GridPane.setHalignment(ridersDepartHigh, HPos.LEFT);
		GridPane.setHalignment(ridersDepartLow, HPos.LEFT);
		GridPane.setHalignment(currentRidersDepart, HPos.LEFT);
		rootCtr.add(ridersDepartLabel, 0, row);
		rootCtr.add(ridersDepartHigh, 1, row);
		rootCtr.add(ridersDepartLow, 2, row++);
		rootCtr.add(currentRidersDepart, 1, row++,  3, 1);
		
		ridersDepartHigh.textProperty().addListener(e -> {
			validate();
		});
		ridersDepartLow.textProperty().addListener(e -> {
			validate();
		});
		
		
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
			
			/*
			 * ridersArrivalHigh, ridersArrivalLow, ridersOffHigh, ridersOffLow, 
			ridersOnHigh, ridersOnLow, ridersDepartHigh, ridersDepartLow;
			 */
			
			// Riders Arrival High, Low
			// Auto complete missing value
			if(!ridersArrivalHigh.getText().isEmpty() && ridersArrivalLow.getText().isEmpty()) {
				userPromptLable.setText("Arrival High value cannot be empty.");
			} else if (ridersArrivalHigh.getText().isEmpty() && !ridersArrivalLow.getText().isEmpty()) {
				userPromptLable.setText("Arrival Low value cannot be empty.");
			}
			if (!ridersArrivalHigh.getText().isEmpty() && !ridersArrivalLow.getText().isEmpty()) {
				
				int high = Integer.parseInt(ridersArrivalHigh.getText());
				int low = Integer.parseInt(ridersArrivalLow.getText());
				
				if(low > high) {
					int temp = high;
					high = low;
					low = temp;
				}
					
				// has data
				currentStop.setRidersArriveHigh(high);
				currentStop.setRidersArriveLow(low);
				
				
				Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Stop Modification");
                alert.setHeaderText("Riders Arrival High, Low change successful.");
                alert.setContentText("");
                alert.show();
                hasSuccess = true;
                
                ridersArrivalHigh.setText("");
                ridersArrivalLow.setText("");
                currentRidersArrival.setText("current: High " + high + ", Low " + low);
			}
			
			// Riders On High, Low
			// Auto complete missing value
			if(!ridersOnHigh.getText().isEmpty() && ridersOnLow.getText().isEmpty()) {
				userPromptLable.setText("On Low value cannot be empty.");
			} else if (ridersOnHigh.getText().isEmpty() && !ridersOnLow.getText().isEmpty()) {
				userPromptLable.setText("On High value cannot be empty.");
			}
			if (!ridersOnHigh.getText().isEmpty() && !ridersOnLow.getText().isEmpty()) {
				
				int high = Integer.parseInt(ridersOnHigh.getText());
				int low = Integer.parseInt(ridersOnLow.getText());
				
				if(low > high) {
					int temp = high;
					high = low;
					low = temp;
				}
					
				// has data
				currentStop.setRidersOnHigh(high);
				currentStop.setRidersOnLow(low);
				
				
				Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Stop Modification");
                alert.setHeaderText("Riders On High, Low change successful.");
                alert.setContentText("");
                alert.show();
                hasSuccess = true;
                
                ridersOnHigh.setText("");
                ridersOnLow.setText("");
                currentRidersOn.setText("current: High " + high + ", Low " + low);
			}
			
			// Riders Off High, Low
			// Auto complete missing value
			if(!ridersOffHigh.getText().isEmpty() && ridersOffLow.getText().isEmpty()) {
				userPromptLable.setText("Off High value cannot be empty.");
			} else if (ridersOffHigh.getText().isEmpty() && !ridersOffLow.getText().isEmpty()) {
				userPromptLable.setText("Off Low value cannot be empty.");
			}
			if (!ridersOffHigh.getText().isEmpty() && !ridersOffLow.getText().isEmpty()) {
				
				int high = Integer.parseInt(ridersOffHigh.getText());
				int low = Integer.parseInt(ridersOffLow.getText());
				
				if(low > high) {
					int temp = high;
					high = low;
					low = temp;
				}
					
				// has data
				currentStop.setRidersOffHigh(high);
				currentStop.setRidersOffLow(low);
				
				
				Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Stop Modification");
                alert.setHeaderText("Riders Off High, Low change successful.");
                alert.setContentText("");
                alert.show();
                hasSuccess = true;
                
                ridersOffHigh.setText("");
                ridersOffLow.setText("");
                currentRidersOff.setText("current: High " + high + ", Low " + low);
			}			
			
			// Riders Depart High, Low
			// Auto complete missing value
			if(!ridersDepartHigh.getText().isEmpty() && ridersDepartLow.getText().isEmpty()) {
				userPromptLable.setText("Depart On High value cannot be empty.");
			} else if (ridersDepartHigh.getText().isEmpty() && !ridersDepartLow.getText().isEmpty()) {
				userPromptLable.setText("Depart Off High value cannot be empty.");
			}
			if (!ridersDepartHigh.getText().isEmpty() && !ridersDepartLow.getText().isEmpty()) {
				
				int high = Integer.parseInt(ridersDepartHigh.getText());
				int low = Integer.parseInt(ridersDepartLow.getText());
				
				if(low > high) {
					int temp = high;
					high = low;
					low = temp;
				}
					
				// has data
				currentStop.setRidersDepartHigh(high);
				currentStop.setRidersDepartLow(low);
				
				
				Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Stop Modification");
                alert.setHeaderText("Riders Depart High, Low change successful.");
                alert.setContentText("");
                alert.show();
                hasSuccess = true;
                
                ridersDepartHigh.setText("");
                ridersDepartLow.setText("");
                currentRidersDepart.setText("current: High " + high + ", Low " + low);
			}			
			
			// if everything is clear, we can close the dialog
			if(hasSuccess
					&& ridersArrivalHigh.getText().isEmpty()
					&& ridersArrivalLow.getText().isEmpty()
					&& ridersOnHigh.getText().isEmpty()
					&& ridersOnLow.getText().isEmpty()
					&& ridersOffHigh.getText().isEmpty()
					&& ridersOffLow.getText().isEmpty()
					&& ridersDepartHigh.getText().isEmpty()
					&& ridersDepartLow.getText().isEmpty()
					) {
				window.close();
			}
			
			
		}); // end setOnAction for okayButton
		
		cancelButton.setOnAction(e -> {
//			System.out.println("Cancel Button pressed");
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		});
		
		
		// Set Default Visible
		userPromptLable.setText("Select a stop from the drop down.");	
		// labels
		ridersArrivalLabel.setVisible(false);
		ridersOffLabel.setVisible(false);
		ridersOnLabel.setVisible(false);
		ridersDepartLabel.setVisible(false);
		currentRidersArrival.setVisible(false);
		currentRidersOff.setVisible(false);
		currentRidersOn.setVisible(false);
		currentRidersDepart.setVisible(false);
		high.setVisible(false);
		low.setVisible(false);
		// input
		ridersArrivalHigh.setVisible(false);
		ridersArrivalLow.setVisible(false);
		ridersOffHigh.setVisible(false);
		ridersOffLow.setVisible(false);
		ridersOnHigh.setVisible(false);
		ridersOnLow.setVisible(false);
		ridersDepartHigh.setVisible(false);
		ridersDepartLow.setVisible(false);
		// breaks
		break1.setVisible(false);
		// buttons
		okayButton.setVisible(false);
		
		
		// Display window and wiat for it to be closed
		Scene scene = new Scene(rootCtr);
		window.setScene(scene);
		window.setResizable(false);
		window.setMinWidth(300);
		window.showAndWait();
	}
	
	private boolean validate() {
		if(stopSelectorComboBox.getValue().isEmpty()) {
			// labels
			ridersArrivalLabel.setVisible(false);
			ridersOffLabel.setVisible(false);
			ridersOnLabel.setVisible(false);
			ridersDepartLabel.setVisible(false);
			currentRidersArrival.setVisible(false);
			currentRidersOff.setVisible(false);
			currentRidersOn.setVisible(false);
			currentRidersDepart.setVisible(false);
			// input
			ridersArrivalHigh.setVisible(false);
			ridersArrivalLow.setVisible(false);
			ridersOffHigh.setVisible(false);
			ridersOffLow.setVisible(false);
			ridersOnHigh.setVisible(false);
			ridersOnLow.setVisible(false);
			ridersDepartHigh.setVisible(false);
			ridersDepartLow.setVisible(false);
			high.setVisible(false);
			low.setVisible(false);
			// breaks
			break1.setVisible(false);
			// buttons
			okayButton.setVisible(false);
			
			userPromptLable.setText("Select a stop from the drop down.");
		} else {
			// labels
			ridersArrivalLabel.setVisible(true);
			ridersOffLabel.setVisible(true);
			ridersOnLabel.setVisible(true);
			ridersDepartLabel.setVisible(true);
			currentRidersArrival.setVisible(true);
			currentRidersOff.setVisible(true);
			currentRidersOn.setVisible(true);
			currentRidersDepart.setVisible(true);
			high.setVisible(true);
			low.setVisible(true);
			// input
			ridersArrivalHigh.setVisible(true);
			ridersArrivalLow.setVisible(true);
			ridersOffHigh.setVisible(true);
			ridersOffLow.setVisible(true);
			ridersOnHigh.setVisible(true);
			ridersOnLow.setVisible(true);
			ridersDepartHigh.setVisible(true);
			ridersDepartLow.setVisible(true);
			// breaks
			break1.setVisible(true);
			// buttons
			okayButton.setVisible(true);
			
			userPromptLable.setText("");
			
			
			
			// RidersArrivalHigh
			if(!ridersArrivalHigh.getText().isEmpty()) {
				if (!ridersArrivalHigh.getText().matches("\\d*")) {
					ridersArrivalHigh.setText(ridersArrivalHigh.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersArrivalHigh.getText().length() > 0
						&& Integer.parseInt(ridersArrivalHigh.getText()) < 0) {
					userPromptLable.setText("Arrival High value must be greater than -1.");
					return false;
				}
			}
			// RidersArrivalLow
			if(!ridersArrivalLow.getText().isEmpty()) {
				if (!ridersArrivalLow.getText().matches("\\d*")) {
					ridersArrivalLow.setText(ridersArrivalLow.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersArrivalLow.getText().length() > 0
						&& Integer.parseInt(ridersArrivalLow.getText()) < 0) {
					userPromptLable.setText("Arrival Low value must be greater than -1.");
					return false;
				}
			}
			
			
			// RidersOffHigh
			if(!ridersOffHigh.getText().isEmpty()) {
				if (!ridersOffHigh.getText().matches("\\d*")) {
					ridersOffHigh.setText(ridersOffHigh.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersOffHigh.getText().length() > 0
						&& Integer.parseInt(ridersOffHigh.getText()) < 0) {
					ridersOffHigh.setText("Off High value must be greater than -1.");
					return false;
				}
			}
			// RidersOffLow
			if(!ridersOffLow.getText().isEmpty()) {
				if (!ridersOffLow.getText().matches("\\d*")) {
					ridersOffLow.setText(ridersOffLow.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersOffLow.getText().length() > 0
						&& Integer.parseInt(ridersOffLow.getText()) < 0) {
					ridersOffLow.setText("Off Low value must be greater than -1.");
					return false;
				}
			}
			
			// RidersOnHigh
			if(!ridersOnHigh.getText().isEmpty()) {
				if (!ridersOnHigh.getText().matches("\\d*")) {
					ridersOnHigh.setText(ridersOnHigh.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersOnHigh.getText().length() > 0
						&& Integer.parseInt(ridersOnHigh.getText()) < 0) {
					ridersOnHigh.setText("On High value must be greater than -1.");
					return false;
				}
			}
			// RidersOnLow
			if(!ridersOnLow.getText().isEmpty()) {
				if (!ridersOnLow.getText().matches("\\d*")) {
					ridersOnLow.setText(ridersOnLow.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersOnLow.getText().length() > 0
						&& Integer.parseInt(ridersOnLow.getText()) < 0) {
					ridersOnLow.setText("On Low value must be greater than -1.");
					return false;
				}
			}
			
			// RidersDepartHigh
			if(!ridersDepartHigh.getText().isEmpty()) {
				if (!ridersDepartHigh.getText().matches("\\d*")) {
					ridersDepartHigh.setText(ridersDepartHigh.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersDepartHigh.getText().length() > 0
						&& Integer.parseInt(ridersDepartHigh.getText()) < 0) {
					ridersDepartHigh.setText("Depart High value must be greater than -1.");
					return false;
				}
			}
			// RidersDepartLow
			if(!ridersDepartLow.getText().isEmpty()) {
				if (!ridersDepartLow.getText().matches("\\d*")) {
					ridersDepartLow.setText(ridersDepartLow.getText().replaceAll("[^\\d]", ""));
		        }
				if(ridersDepartLow.getText().length() > 0
						&& Integer.parseInt(ridersDepartLow.getText()) < 0) {
					ridersDepartLow.setText("Depart Low value must be greater than -1.");
					return false;
				}
			}	
			
		}
		return true;
	}
}
