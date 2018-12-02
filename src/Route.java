import java.util.ArrayList;
import java.util.List;

public class Route 
{
	private int id;
	private  int number;
	private String name;
	private  List<Stop> stops;
	
	public Route(int id, int number, String name) 
	{
		this.id =id;
		this.number = number;
		this.name = name;
		
		stops = new ArrayList<>();
	}
	
	public void extendRoute(Stop stop)
	{
		stops.add(stop);
	}

	public int getId()
	{
		return id;
	}

	public int getNumber()
	{
		return number;
	}

	public String getName()
	{
		return name;
	}

	public List<Stop> getStops()
	{
		return stops;
	}

	public Stop getStop(int stopIndex) {
		return stops.get(stopIndex);
	}
}
