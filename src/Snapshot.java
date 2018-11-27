import java.util.List;

public class Snapshot 
{
	private List<Bus> buses;
	private List<Stop> stops;
	private Event event;
	
	public Snapshot(List<Bus> buses, List<Stop> stops, Event event)
	{
		this.buses = buses;
		this.stops = stops;
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
	
	public Event getEvent()
	{
		return this.event;
	}
	
}
