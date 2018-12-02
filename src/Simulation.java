import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Simulation
{
	protected int currentTime;
	private double kSpeed;
	private double kCapacity;
	private double kWaiting;
	private double kBuses;
	private double kCombined;
	protected List<Bus> buses;
	protected List<Stop> stops;
	protected List<Route> routes;
	protected List<Event> events;
	protected List<Depot> depots;
	protected List<Snapshot> snapshots;
	public Simulation(String filename, int iterations)
	{
		currentTime = 0;
		kSpeed = 1.0;
		kCapacity = 1.0;
		kWaiting = 1.0;
		kBuses = 1.0;
		kCombined = 1.0;
		buses = new ArrayList<>();
		stops = new ArrayList<>();
		routes = new ArrayList<>();
		events = new ArrayList<>();
		depots = new ArrayList<>();
		snapshots = new ArrayList<>();

		readFile(filename);
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

	private int waitingPassengers()
	{
		int passengers = 0;
		for (int i = 0; i < stops.size(); i++)
		{
			passengers += stops.get(i).getNumRiders();
		}
		return passengers;
	}

	private double busCost()
	{
		double cost = 0;
		Bus bus;
		for (int i = 0; i < buses.size(); i++)
		{
			bus = buses.get(i);
			cost += kSpeed * bus.getSpeed() + kCapacity * bus.getCapacity();
		}
		return cost;
	}

	private void readFile(String filename)
	{
		try(FileReader fileReader = new FileReader(filename); BufferedReader bufferedReader =  new BufferedReader(fileReader))
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
			System.out.println("Unable to open file '" + filename + "'.");
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + filename + "'.");
		}
	}

	private void addDepot(int id, String name, double latitude, double longitude)
	{
		Depot depot = new Depot(id,name,latitude,longitude);
		depots.add(depot);
	}

	private void addStop(int id, String name, int num, double latitude, double longitude)
	{
		Stop stop = new Stop(id,name,num,latitude,longitude);
		stops.add(stop);
	}

	private void addRoute(int id, int number, String name)
	{
		Route route = new Route(id, number, name);
		routes.add(route);
	}

	private void extendRoute(int routeId, int stopId)
	{
		for (int i = 0; i < stops.size(); i++)
		{
			if(stops.get(i).getId() == stopId)
			{
				for (int j = 0; j < routes.size(); j++)
				{
					if(routes.get(j).getId() == routeId)
					{
						routes.get(j).extendRoute(stops.get(i));
					}
				}
			}
		}

	}

	private void addBus(int id, int initialRouteId, int currentStopIndex, int initialCapacity, int initialSpeed)
	{
		Bus bus = new Bus(id, initialRouteId, currentStopIndex, initialCapacity, initialSpeed);
		buses.add(bus);
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
		for(int i= 0; i < buses.size(); i++)
		{
			for (int j=0; j < routes.size(); j++)
			{
				if (buses.get(i).getRouteId() == routes.get(j).getId())
				{
					buses.get(i).setRoute(routes.get(j));
				}
			}
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

				for (int j=0; j<buses.size();j++)
				{
					Bus bus = buses.get(j);
					if(bus.getId() == events.get(0).getId())
					{
						Event e = events.get(0); //Save for Snapshot
						events.remove(0);

						//todo: Shakiem (for review) I think we should be creating the snapshot here, instead of after the bus leaves

						//bus arrives at stop
						Route currentRoute = bus.getRoute();
						Stop currentStop = currentRoute.getStop(bus.getCurrentStopIndex());
						//simulate new riders arriving to the stop
						currentStop.ridersArrive();
						//off load bus
						int ridersOff = bus.ridersOff();
						currentStop.addTransfers(ridersOff);
						//board bus
						currentStop.ridersOn(bus);
						//simulate passengers leaving
						currentStop.ridersDepart();

						newTime = bus.nextStopTime(currentTime);
						addEvent(newTime,"move_bus", bus.getId());
						//Add System snapshot for rewind functionality
						Snapshot snapshot = new Snapshot(buses, stops, routes, e);
						snapshots.add(0,snapshot);
						//Only 3 System snapshots are ever saved at one time
						if (snapshots.size() > 3)
						{
							snapshots.remove(3);
						}
						break;
					}
				}
			}
			count++;
		}
	}
}