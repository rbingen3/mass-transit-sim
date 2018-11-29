import java.util.List;

public class Snapshot 
{
	private List<Bus> buses;
	private List<Stop> stops;
	private List<Route> routes;
	private Event event;
	
	public Snapshot(List<Bus> buses, List<Stop> stops, List<Route> routes, Event event)
	{
		this.buses = buses;
		this.stops = stops;
		this.routes = routes;
		this.event = event;
	}
	
	public List<Bus> getBuses()
	{
		return this.buses;
	}
	
	public List<Stop> getStops()
	{
		return this.stops;
	}
	
	public List<Route> getRoutes()
	{
		return this.routes;
	}
	
	public Event getEvent()
	{
		return this.event;
	}
	
}
