import java.util.ArrayList;
import java.util.List;

public class Route 
{
	public int id;
	public  int number;
	public String name;
	public List<Stop> stops;
	
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
	

}
