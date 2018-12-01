public class Event implements Comparable
{
	private int id;
	private int time;
	private String type;
	
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

	public int getId()
	{
		return id;
	}

	public int getTime()
	{
		return time;
	}

	public String getType()
	{
		return type;
	}
}
