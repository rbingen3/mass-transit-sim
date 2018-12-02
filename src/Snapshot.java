import java.util.Map;

public class Snapshot 
{
	private Map<Integer, Bus> buses;
	private Map<Integer, Stop> stops;
	private Map<Integer, Route> routes;
	private Event event;
	
	public Snapshot(Map<Integer, Bus> buses, Map<Integer, Stop> stops, Map<Integer, Route> routes, Event event)
	{
		this.buses = buses;
		this.stops = stops;
		this.routes = routes;
		this.event = event;
	}
	
	public Map<Integer, Bus> getBuses()
	{
		return this.buses;
	}
	
	public Map<Integer, Stop> getStops()
	{
		return this.stops;
	}
	
	public Map<Integer, Route> getRoutes()
	{
		return this.routes;
	}
	
	public Event getEvent()
	{
		return this.event;
	}
	
}
