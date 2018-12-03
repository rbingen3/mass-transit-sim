import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EfficiencyDialog {	
	
	Button cancelButton;
	
	
	public EfficiencyDialog(){
	}
	
	public void display(Simulation sim) {
		Stage window = new Stage();
		
		// Block event to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("System Efficiency"); 
		
		// Make grid for user input
		VBox rootCtr = new VBox();
		rootCtr.setPadding(new Insets(10));
		
		int row = 0;
		
		// HR
		Label break1 = new Label("Buses");
		break1.setStyle("-fx-font-weight: bold");
		GridPane.setHalignment(break1, HPos.LEFT);
		rootCtr.getChildren().add(break1);
		
		
		// Table
		TableView<Bus> table = new TableView<Bus>();

		// Create column names
		TableColumn<Bus, Integer> busIdCol //
				= new TableColumn<Bus, Integer>("Bus Id");
		TableColumn<Bus, String> busRouteCol//
				= new TableColumn<Bus, String>("Route");
		 
		// Create 2 sub column for FullName.
		TableColumn<Bus, String> busRidersFullCol
        = new TableColumn<Bus, String>("Riders");
		TableColumn<Bus, Integer> busRidersCol
				= new TableColumn<Bus, Integer>("Total");
		TableColumn<Bus, Integer> busCapacityCol 
				= new TableColumn<Bus, Integer>("Capacity");
		busRidersFullCol.getColumns().addAll(busRidersCol, busCapacityCol);
		
		TableColumn<Bus, Integer> busSpeedCol
				= new TableColumn<Bus, Integer>("Speed");
		//TableColumn<Bus, Integer> busArrivialTimeCol 
		//		= new TableColumn<Bus, Integer>("Arrivial Time");
 
		table.getColumns().addAll(busIdCol, busRouteCol, busRidersFullCol, 
				busSpeedCol); //, busArrivialTimeCol);
		
		// Defines how to fill data for each cell.
		// Get value from property of UserAccount.
		busIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		busRouteCol.setCellValueFactory(new PropertyValueFactory<>("routeId"));
		busRidersCol.setCellValueFactory(new PropertyValueFactory<>("numRiders"));
		busCapacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
		busSpeedCol.setCellValueFactory(new PropertyValueFactory<>("speed"));
		//busArrivialTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
		
		// Set Sort type for userName column
		busIdCol.setSortType(TableColumn.SortType.DESCENDING);
	    //lastNameCol.setSortable(false);
		
		// Display row data
	    ObservableList<Bus> list = getBusesList(sim);
	    table.setItems(list);
	    
	    rootCtr.getChildren().add(table);
	    
	    
	    
	    
		// HR
		Label break2 = new Label("Stops");
		break2.setStyle("-fx-font-weight: bold");
		GridPane.setHalignment(break2, HPos.LEFT);
		rootCtr.getChildren().add(break2);
		
		
		
		// Table
		TableView<Stop> table2 = new TableView<Stop>();

		// Create column names
		TableColumn<Stop, Integer> stopIdCol 
				= new TableColumn<Stop, Integer>("Stop Id");
		TableColumn<Stop, String> stopNameCol
				= new TableColumn<Stop, String>("Name");
		 
		TableColumn<Stop, Integer> stopRidersCol
				= new TableColumn<Stop, Integer>("Riders");
		
		TableColumn<Stop, Integer> stopLatCol
				= new TableColumn<Stop, Integer>("Latitude");
		TableColumn<Stop, Integer> stopLongCol
				= new TableColumn<Stop, Integer>("Longitude");
		
		TableColumn<Stop, String> stopArriveCol
				= new TableColumn<Stop, String>("Riders Arrive");
		TableColumn<Stop, Integer> stopArriveHighCol
			= new TableColumn<Stop, Integer>("High");
		TableColumn<Stop, Integer> stopArriveLowCol
			= new TableColumn<Stop, Integer>("Low");
		stopArriveCol.getColumns().addAll(stopArriveHighCol, stopArriveLowCol);
		
		TableColumn<Stop, String> stopDepartCol
			= new TableColumn<Stop, String>("Riders Depart");
		TableColumn<Stop, Integer> stopDepartHighCol
			= new TableColumn<Stop, Integer>("High");
		TableColumn<Stop, Integer> stopDepartLowCol
			= new TableColumn<Stop, Integer>("Low");
		stopDepartCol.getColumns().addAll(stopDepartHighCol, stopDepartLowCol);		
		
		TableColumn<Stop, String> stopOnCol
			= new TableColumn<Stop, String>("Riders On");
		TableColumn<Stop, Integer> stopOnHighCol
			= new TableColumn<Stop, Integer>("High");
		TableColumn<Stop, Integer> stopOnLowCol
			= new TableColumn<Stop, Integer>("Low");
		stopOnCol.getColumns().addAll(stopOnHighCol, stopOnLowCol);			
		
		TableColumn<Stop, String> stopOffCol
			= new TableColumn<Stop, String>("Riders Off");
		TableColumn<Stop, Integer> stopOffHighCol
			= new TableColumn<Stop, Integer>("High");
		TableColumn<Stop, Integer> stopOffLowCol
			= new TableColumn<Stop, Integer>("Low");
		stopOffCol.getColumns().addAll(stopOffHighCol, stopOffLowCol);			
		
 
		table2.getColumns().addAll(stopIdCol, stopNameCol, stopRidersCol, 
				stopLatCol, stopLongCol, stopArriveCol, stopDepartCol, stopOnCol, stopOffCol);
		
		// Defines how to fill data for each cell.
		// Get value from property of UserAccount.
		stopIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		stopNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		stopRidersCol.setCellValueFactory(new PropertyValueFactory<>("numRiders"));
		stopLatCol.setCellValueFactory(new PropertyValueFactory<>("latitude"));
		stopLongCol.setCellValueFactory(new PropertyValueFactory<>("longitude"));
		stopArriveHighCol.setCellValueFactory(new PropertyValueFactory<>("ridersArriveHigh"));
		stopArriveLowCol.setCellValueFactory(new PropertyValueFactory<>("ridersArriveLow"));
		stopDepartHighCol.setCellValueFactory(new PropertyValueFactory<>("ridersDepartHigh"));
		stopDepartLowCol.setCellValueFactory(new PropertyValueFactory<>("ridersDepartLow"));
		stopOnHighCol.setCellValueFactory(new PropertyValueFactory<>("ridersOnHigh"));
		stopOnLowCol.setCellValueFactory(new PropertyValueFactory<>("ridersOnLow"));
		stopOffHighCol.setCellValueFactory(new PropertyValueFactory<>("ridersOffHigh"));
		stopOffLowCol.setCellValueFactory(new PropertyValueFactory<>("ridersOffLow"));		
		
		
		// Set Sort type for userName column
		busIdCol.setSortType(TableColumn.SortType.DESCENDING);
	    //lastNameCol.setSortable2(false);
		
		// Display row data
	    ObservableList<Stop> list2 = getStopsList(sim);
	    table2.setItems(list2);
	    
	    int startingRow = row;
	    rootCtr.getChildren().add(table2);
		
		
		
		// Submission Buttons
		HBox rootCtr_buttonCtr = new HBox();
		GridPane.setHalignment(rootCtr_buttonCtr, HPos.LEFT);
		rootCtr.getChildren().add(rootCtr_buttonCtr);
		
		cancelButton = new Button("Close");
		rootCtr_buttonCtr.getChildren().add(cancelButton);
		
		cancelButton.setOnAction(e -> {
//			System.out.println("Cancel Button pressed");
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		});
		
		
		
		// Display window and wiat for it to be closed
		Scene scene = new Scene(rootCtr);
		window.setScene(scene);
		window.setResizable(false);
		window.showAndWait();
	}
	
	  private ObservableList<Bus> getBusesList(Simulation sim) {
	      ObservableList<Bus> list = FXCollections
	    		  .observableArrayList(sim.buses.values());
	      return list;
	  }
	  
	  private ObservableList<Stop> getStopsList(Simulation sim) {
	      ObservableList<Stop> list = FXCollections
	    		  .observableArrayList(sim.stops.values());
	      return list;
	  }
	  
}
