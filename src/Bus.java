public class Bus 
{
	public int id;
	public int routeId;
	public int currentStop;
	public int numRiders;
	private int capacity;
	private int fuel;
	private int fuelCapacity;
	private int speed;
	private int arrivalTime;
	private Route route;
	
	
	public Bus (int id, int route, int currentStop, int numRiders, int capacity, int fuel, int fuelCapacity, int speed)
	{
		this.id = id;
		this.routeId = route;
		this.currentStop = currentStop;
		this.numRiders = numRiders;
		this.capacity = capacity;
		this.fuelCapacity = fuelCapacity;
		this.speed = speed;
	}
	
	public void addPassengers(int num)
	{
		numRiders += num;
	}
	
	public void disembarkPassengers(int num)
	{
		numRiders -= num;
	}
	
	public int nextStopTime(int time)
	{
		return calculateNextArrivalTime(time);
	}
	
	public void setRoute(Route route)
	{
		this.route = route;
	}

	public int getSpeed()
	{
		return speed;
	}

	public int getCapacity()
	{
		return capacity;
	}
	
	private int calculateNextArrivalTime(int time)
	{
		int retVal = 0;
		int travelTime = 0;
		double lat1,lat2,long1,long2 = 0.0;
	
		if (currentStop >= (route.stops.size() - 1 ))
		{
			lat1 = route.stops.get(currentStop).latitude;
			lat2 = route.stops.get(0).latitude;
			long1 = route.stops.get(currentStop).longitude;
			long2 = route.stops.get(0).longitude;
			
			double distance = 70.0 * Math.sqrt(Math.pow((lat1-lat2), 2)+Math.pow((long1-long2), 2));
			travelTime = 1 + ((int) distance * 60 / speed);
			if(travelTime < 1)
			{
				retVal = time + 1;
			}
			else
			{
				retVal = time + travelTime;
			}
			currentStop = 0;
			
		} else {			
			lat1 = route.stops.get(currentStop).latitude;
			lat2 = route.stops.get(currentStop + 1).latitude;
			long1 = route.stops.get(currentStop).longitude;
			long2 = route.stops.get(currentStop+ 1).longitude;
			
			double distance = 70.0 * Math.sqrt(Math.pow((lat1-lat2), 2)+Math.pow((long1-long2), 2));
			travelTime = 1 + ((int) distance * 60 / speed);
			if(travelTime < 1)
			{
				retVal = time + 1;
			}
			else
			{
				retVal = time + travelTime;
			}			
			currentStop++;
		}
		System.out.println("b:"+id+"->s:"+route.stops.get(currentStop).id+"@"+retVal+"//p:0/f:0");	
		return retVal;
	}
	
}
