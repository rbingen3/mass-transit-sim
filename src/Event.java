public class Event implements Comparable
{
	public int id;
	public int time;
	public String type;
	
	public Event(int time, String type, int id)
	{
		this.id = id;
		this.time = time;
		this.type =  type;
	}

	@Override
	public int compareTo(Object arg0) {
		int compare = ((Event) arg0).time;
		
		return this.time - compare;
	}
}
