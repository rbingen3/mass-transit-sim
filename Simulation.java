import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation 
{
	private int currentTime;
	private List<Bus> buses;
	private List<Stop> stops;
	private List<Route> routes;
	private List<Event> events;
	private List<Depot> depots;
	private List<Snapshot> snapshots;
	
	
	public Simulation (String filename, int iterations)
	{
		currentTime = 0;
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
			currentTime = snapshots.get(0).getEvent().time;
			sortEvents();
			snapshots.remove(0);
		}
	}
	
	private void readFile(String filename)
	{
		try {
			String line = null;
            FileReader fileReader = new FileReader(filename);

            BufferedReader bufferedReader =  new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                String[] temp = line.split(",");
                
                if(temp[0].equals("add_depot"))
                {
                	addDepot(Integer.parseInt(temp[1]),temp[2],Double.parseDouble(temp[3]),Double.parseDouble(temp[4]));
                }
                else if(temp[0].equals("add_stop"))
                {
                	addStop(Integer.parseInt(temp[1]), temp[2], Integer.parseInt(temp[3]), Double.parseDouble(temp[4]), Double.parseDouble(temp[5]));
                }
                else if(temp[0].equals("add_route"))
                {
                	addRoute(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), temp[3]);
                }
                else if(temp[0].equals("extend_route"))
                {
                	extendRoute(Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
                }
                else if(temp[0].equals("add_bus"))
                {
                	addBus(Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]),Integer.parseInt(temp[5]),Integer.parseInt(temp[6]),Integer.parseInt(temp[7]),Integer.parseInt(temp[8]));
                }
                else if(temp[0].equals("add_event"))
                {
                	addEvent(Integer.parseInt(temp[1]),temp[2], Integer.parseInt(temp[3]));
                }
                
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filename + "'");                  
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
			if(stops.get(i).id == stopId)
			{
				for (int j = 0; j < routes.size(); j++)
				{
					if(routes.get(j).id == routeId)
					{
						routes.get(j).extendRoute(stops.get(i));
					}
				}					
			}
		}
			
	}
	
	private void addBus(int id, int route, int currentStop, int numRiders, int capacity, int fuel, int fuelCapacity, int speed)
	{
		Bus bus = new Bus( id, route, currentStop, numRiders, capacity, fuel, fuelCapacity, speed);
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
				if (buses.get(i).routeId == routes.get(j).id)
				{
					buses.get(i).setRoute(routes.get(j));
				}
			}
		}
	}
	
	private void runSimulation(int iterations)
	{
		int count = 0;
		while (events.size() > 0 && count < iterations)
		{
			//System.out.println(count);
			if (events.get(0).type.equals("move_bus"))
			{
				currentTime = events.get(0).time;
				int newTime = -1;
				
				for (int j=0; j<buses.size();j++)
				{
					if(buses.get(j).id == events.get(0).id)
					{
						Event e = events.get(0); //Save for Snapshot
						events.remove(0);
						newTime = buses.get(j).nextStopTime(currentTime);
						addEvent(newTime,"move_bus",buses.get(j).id);
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
