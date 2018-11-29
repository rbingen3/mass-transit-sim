public class Stop 
{
	public int id;
	public String name;
	public int numRiders;
	public double latitude;
	public double longitude;
	
	public Stop (int id, String name, int numRiders, double latitude, double longitude)
	{
		this.id = id;
		this.name = name;
		this.numRiders = numRiders;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public void addPassengersToStop(int num)
	{
		numRiders += num;
	}
	
	public void passengersGetOnBus(int num)
	{
		numRiders -= num;
	}

	public int getNumRiders()
	{
		return numRiders;
	}
}
