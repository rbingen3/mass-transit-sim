import java.util.Random;

public class Bus
{
	private int id;
	private int routeId;
	private int currentStopIndex;
	private int numRiders;
	private int capacity;
	private int fuel;
	private int fuelCapacity;
	private int speed;
	private int arrivalTime;
	private Route route;
	private int ridersOffHigh = 20;
	private int ridersOffLow = 1;
	private Random randomGenerator;

	
	
	public Bus (int id, int route, int currentStopIndex, int numRiders, int capacity, int fuel, int fuelCapacity, int speed)
	{
		this.id = id;
		this.routeId = route;
		this.currentStopIndex = currentStopIndex;
		this.numRiders = numRiders;
		this.capacity = capacity;
		this.fuelCapacity = fuelCapacity;
		this.speed = speed;
		this.numRiders = 0;
		randomGenerator = new Random();
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

	public int ridersOff()
	{
		int ridersGettingOff = randomGenerator.nextInt( (ridersOffHigh - ridersOffLow) + 1) + ridersOffLow;
		if(numRiders < ridersGettingOff)
			ridersGettingOff = numRiders;

		numRiders -= ridersGettingOff;

		return ridersGettingOff;
	}

	public void setRoute(Route route)
	{
		this.route = route;
		this.routeId = route.id;
	}

	public int getSpeed()
	{
		return speed;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public int getId()
	{
		return id;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId){
		this.routeId = routeId;
	}

	public int getNumRiders(){
		return this.numRiders;
	}

	public int getCurrentStopIndex()
	{
		return currentStopIndex;
	}

	private int calculateNextArrivalTime(int time)
	{
		int travelTime = 0;
		double lat1,lat2,long1,long2 = 0.0;
	
		if (currentStopIndex >= (route.stops.size() - 1 ))
		{
			lat1 = route.stops.get(currentStopIndex).latitude;
			lat2 = route.stops.get(0).latitude;
			long1 = route.stops.get(currentStopIndex).longitude;
			long2 = route.stops.get(0).longitude;
			
			double distance = 70.0 * Math.sqrt(Math.pow((lat1-lat2), 2)+Math.pow((long1-long2), 2));
			travelTime = 1 + ((int) distance * 60 / speed);
			if(travelTime < 1)
			{
				arrivalTime = time + 1;
			}
			else
			{
				arrivalTime = time + travelTime;
			}
			currentStopIndex = 0;
			
		} else {			
			lat1 = route.stops.get(currentStopIndex).latitude;
			lat2 = route.stops.get(currentStopIndex + 1).latitude;
			long1 = route.stops.get(currentStopIndex).longitude;
			long2 = route.stops.get(currentStopIndex + 1).longitude;
			
			double distance = 70.0 * Math.sqrt(Math.pow((lat1-lat2), 2)+Math.pow((long1-long2), 2));
			travelTime = 1 + ((int) distance * 60 / speed);
			if(travelTime < 1)
			{
				arrivalTime = time + 1;
			}
			else
			{
				arrivalTime = time + travelTime;
			}			
			currentStopIndex++;
		}
		System.out.println("b:"+id+"->s:"+route.stops.get(currentStopIndex).id+"@"+arrivalTime+"//p:"+numRiders+"/f:0");
		return arrivalTime;
	}

	public int getBoardingCapacity()
	{
		return capacity - numRiders;
	}
}
