import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.*;

public class Simulation
{
	protected int currentTime;
	private double kSpeed;
	private double kCapacity;
	private double kWaiting;
	private double kBuses;
	private double kCombined;
	protected Map<Integer, Bus> buses;
	protected Map<Integer, Stop> stops;
	protected Map<Integer, Route> routes;
	protected List<Event> events;
	protected Map<Integer, Depot> depots;
	protected List<Snapshot> snapshots;

	public Simulation(String scenarioFileName, String probabilityFileName, int iterations)
	{
		currentTime = 0;
		kSpeed = 1.0;
		kCapacity = 1.0;
		kWaiting = 1.0;
		kBuses = 1.0;
		kCombined = 1.0;
		buses = new HashMap<>();
		stops = new HashMap<>();
		routes = new HashMap<>();
		events = new ArrayList<>();
		depots = new HashMap<>();
		snapshots = new ArrayList<>();

		deserializeConfigurations(scenarioFileName, probabilityFileName);
		initializeRoutes();
		runSimulation(iterations);
	}
	//Goes Back One Event each time called
	public void rewindSimulation()
	{
		if (snapshots.size() > 0)
		{
			buses = snapshots.get(0).getBuses();
			stops = snapshots.get(0).getStops();
			routes = snapshots.get(0).getRoutes();
			events.add(snapshots.get(0).getEvent());
			currentTime = snapshots.get(0).getEvent().getTime();
			sortEvents();
			snapshots.remove(0);
		}
	}

	public double getSystemEfficiency()
	{
		int passengers = waitingPassengers();
		double cost = busCost();
		return kWaiting * passengers + kBuses * cost +
			kCombined * passengers * cost;
	}

	public void setKSpeed(double k)
	{
		this.kSpeed = k;
	}

	public void setKCapacity(double k)
	{
		this.kCapacity = k;
	}

	public void setKWaiting(double k)
	{
		this.kWaiting = k;
	}

	public void setKBuses(double k)
	{
		this.kBuses = k;
	}

	public void setKCombined(double k)
	{
		this.kCombined = k;
	}

	public void updateBusSpeed(Bus bus, int speed)
	{
		bus.updateSpeed(speed);
	}

	public void updateBusCapacity(Bus bus, int capacity)
	{
		bus.updateCapacity(capacity);
	}

	public void updateBusRoute(Bus bus, Route route, Stop stop)
	{
		bus.updateRoute(route, stop);
	}

	private int waitingPassengers()
	{
		int passengers = 0;
		for (Map.Entry stopEntry : stops.entrySet())
		{

			passengers += ((Stop)stopEntry.getValue()).getNumRiders();
		}
		return passengers;
	}

	private double busCost()
	{
		double cost = 0;
		Bus bus;
		for (Map.Entry busEntry : buses.entrySet())
		{
			bus = (Bus)busEntry.getValue();
			cost += kSpeed * bus.getSpeed() + kCapacity * bus.getCapacity();
		}
		return cost;
	}

	private void deserializeConfigurations(String scenarioFileName, String probabilityFileName)
	{
		readInScenario(scenarioFileName);
		readInProbabilities(probabilityFileName);
	}

	private void readInScenario(String scenarioFileName) {
		try(FileReader fileReader = new FileReader(scenarioFileName); BufferedReader bufferedReader =  new BufferedReader(fileReader))
		{
			String line;

			while((line = bufferedReader.readLine()) != null) {
				String[] commandTokens = line.split(",");
				String command = commandTokens[0];

				switch(command)
				{
					case "add_depot":
						addDepot(Integer.parseInt(commandTokens[1]),commandTokens[2],Double.parseDouble(commandTokens[3]),Double.parseDouble(commandTokens[4]));
						break;
					case "add_stop":
						addStop(Integer.parseInt(commandTokens[1]), commandTokens[2], Integer.parseInt(commandTokens[3]), Double.parseDouble(commandTokens[4]), Double.parseDouble(commandTokens[5]));
						break;
					case "add_route":
						addRoute(Integer.parseInt(commandTokens[1]), Integer.parseInt(commandTokens[2]), commandTokens[3]);
						break;
					case "extend_route":
						extendRoute(Integer.parseInt(commandTokens[1]),Integer.parseInt(commandTokens[2]));
						break;
					case "add_bus":
						addBus(Integer.parseInt(commandTokens[1]),Integer.parseInt(commandTokens[2]),Integer.parseInt(commandTokens[3]),Integer.parseInt(commandTokens[4]),Integer.parseInt(commandTokens[5]));
						break;
					case "add_event":
						addEvent(Integer.parseInt(commandTokens[1]),commandTokens[2], Integer.parseInt(commandTokens[3]));
						break;
					default:
						System.out.println("'" + command + "' is not a recognized command.");
				}
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + scenarioFileName + "'.");
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + scenarioFileName + "'.");
		}
	}

	private void readInProbabilities(String probabilityFileName)
	{
		try(FileReader fileReader = new FileReader(probabilityFileName); BufferedReader bufferedReader =  new BufferedReader(fileReader))
		{
			String line;

			while((line = bufferedReader.readLine()) != null) {
				String[] probabilityParameters = line.split(",");
				int stopID = Integer.parseInt(probabilityParameters[0]);
				Stop stop = stops.get(stopID);
				if(stop != null)
					updateStopRiderProbability(stop, Integer.parseInt(probabilityParameters[1]), Integer.parseInt(probabilityParameters[2]), Integer.parseInt(probabilityParameters[3]),
											Integer.parseInt(probabilityParameters[4]), Integer.parseInt(probabilityParameters[5]), Integer.parseInt(probabilityParameters[6]),
											Integer.parseInt(probabilityParameters[7]), Integer.parseInt(probabilityParameters[8]));
			}
		}
		catch(FileNotFoundException ex) {
			String errorMsg = "Unable to open file '" + probabilityFileName + "'." + "\n" +
					"Using system defaults for stop distribution.";
			System.out.println(errorMsg);
			Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Configuration Error.");
					alert.setContentText(errorMsg);
					alert.show();
			});
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + probabilityFileName + "'.");
			System.out.println("Using system defaults for stop distribution.");
		}
	}

	private void updateStopRiderProbability(Stop stop, int arriveHigh, int arriveLow, int offHigh, int offLow, int onHigh, int onLow, int departHigh, int departLow)
	{
		stop.setRidersArriveHigh(arriveHigh);
		stop.setRidersArriveLow(arriveLow);
		stop.setRidersOffHigh(offHigh);
		stop.setRidersOffLow(offLow);
		stop.setRidersOnHigh(onHigh);
		stop.setRidersOnLow(onLow);
		stop.setRidersDepartHigh(departHigh);
		stop.setRidersDepartLow(departLow);
	}

	private void addDepot(int id, String name, double latitude, double longitude)
	{
		Depot depot = new Depot(id,name,latitude,longitude);
		depots.put(depot.getId(), depot);
	}

	private void addStop(int id, String name, int num, double latitude, double longitude)
	{
		Stop stop = new Stop(id,name,num,latitude,longitude);
		stops.put(stop.getId(), stop);
	}

	private void addRoute(int id, int number, String name)
	{
		Route route = new Route(id, number, name);
		routes.put(route.getId(), route);
	}

	private void extendRoute(int routeId, int stopId)
	{
		Route route = routes.get(routeId);
		Stop stop = stops.get(stopId);
		route.extendRoute(stop);
	}

	private void addBus(int id, int initialRouteId, int currentStopIndex, int initialCapacity, int initialSpeed)
	{
		Bus bus = new Bus(id, initialRouteId, currentStopIndex, initialCapacity, initialSpeed);
		buses.put(bus.getId(), bus);
	}

	private void addEvent(int time, String type, int id)
	{
		Event event = new Event(time, type, id);
		events.add(event);
		sortEvents();
	}

	private void sortEvents()
	{
		Collections.sort(events);
	}

	private void incrementTime()
	{
		currentTime++;
	}

	private void initializeRoutes()
	{
		for(Map.Entry busEntry: buses.entrySet())
		{
			Bus bus = (Bus) busEntry.getValue();
			Route route = routes.get(bus.getRouteId());
			bus.setRoute(route);
		}
	}

	protected void runSimulation(int iterations)
	{
		int count = 0;
		while (events.size() > 0 && count < iterations)
		{
			//System.out.println(count);
			if (events.get(0).getType().equals("move_bus"))
			{
				currentTime = events.get(0).getTime();
				int newTime = -1;

				Event e = events.get(0); //Save for Snapshot
				events.remove(0);
				Bus bus = buses.get(e.getId());

				//************************bus arrives at stop******************************

				//Add System snapshot for rewind functionality
				HashMap<Integer, Bus> busesSnap = this.copyBus(buses);
				HashMap<Integer, Stop> stopsSnap= this.copyStop(stops);
				HashMap<Integer, Route> routesSnap= this.copyRoute(routes);
				Snapshot snapshot = new Snapshot(busesSnap, stopsSnap, routesSnap, e);
				snapshots.add(0,snapshot);
				//Only 3 System snapshots are ever saved at one time
				if (snapshots.size() > 3)
				{
					snapshots.remove(3);
				}

				Route currentRoute = bus.getRoute();
				Stop currentStop = currentRoute.getStop(bus.getCurrentStopIndex());

				//Handle Updated Bus parameters now:
				bus.updateParams();

				//simulate new riders arriving to the stop
				currentStop.ridersArrive();
				//off load bus
				int ridersOff = currentStop.ridersOff(bus);
				currentStop.addTransfers(ridersOff);
				//board bus
				currentStop.ridersOn(bus);
				//simulate passengers leaving
				currentStop.ridersDepart();

				newTime = bus.nextStopTime(currentTime);
				addEvent(newTime,"move_bus", bus.getId());
			}
			count++;
		}
	}
	
	public  HashMap<Integer, Bus> copyBus(
		    Map<Integer, Bus> original)
	{
	    HashMap<Integer, Bus> copy = new HashMap<Integer, Bus>();
	    for (Map.Entry<Integer, Bus> entry : original.entrySet())
	    {
	        copy.put(entry.getKey(),
	           new Bus(entry.getValue().getId(),entry.getValue().getRouteId(),entry.getValue().getCurrentStopIndex(),entry.getValue().getCapacity(), entry.getValue().getSpeed()));
	    }
	    return copy;
	}
	
	public  HashMap<Integer, Stop> copyStop(
		    Map<Integer, Stop> original)
	{
	    HashMap<Integer, Stop> copy = new HashMap<Integer, Stop>();
	    for (Map.Entry<Integer, Stop> entry : original.entrySet())
	    {
	        copy.put(entry.getKey(),
	           new Stop(entry.getValue().getId(),entry.getValue().getName(),entry.getValue().getNumRiders(),entry.getValue().getLatitude(), entry.getValue().getLongitude()));
	    }
	    return copy;
	}
	
	public  HashMap<Integer, Route> copyRoute(
		    Map<Integer, Route> original)
	{
	    HashMap<Integer, Route> copy = new HashMap<Integer, Route>();
	    for (Map.Entry<Integer, Route> entry : original.entrySet())
	    {
	        copy.put(entry.getKey(),
	           new Route(entry.getValue().getId(),entry.getValue().getNumber(),entry.getValue().getName()));
	    }
	    return copy;
	}
}