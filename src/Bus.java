import java.util.Random;

public class Bus
{
	private int id;
	private int routeId;
	private int currentStopIndex;
	private int numRiders;
	private int capacity;
	private int speed;
	private int arrivalTime;
	private Route route;
	private int ridersOffHigh;
	private int ridersOffLow;
	private Random randomGenerator;

	
	
	public Bus (int id, int initialRouteId, int currentStopIndex, int initialCapacity, int initialSpeed)
	{
		this.id = id;
		this.routeId = initialRouteId;
		this.currentStopIndex = currentStopIndex;
		this.capacity = initialCapacity;
		this.speed = initialSpeed;
		this.numRiders = 0;
		this.ridersOffLow = 1;
		this.ridersOffHigh = 20;
		this.randomGenerator = new Random();
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

	public int getCurrentStopIndex()
	{
		return currentStopIndex;
	}

	public int getNumRiders(){
		return this.numRiders;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public int getSpeed()
	{
		return speed;
	}

	public Route getRoute()
	{
		return route;
	}

	public void setRoute(Route route)
	{
		this.route = route;
		this.routeId = route.getId();
	}
	
	public void addPassengers(int num)
	{
		numRiders += num;
	}
	
	public int nextStopTime(int time)
	{
		return calculateNextArrivalTime(time);
	}

	public int ridersOff()
	{
		int ridersGettingOff = randomGenerator.nextInt((ridersOffHigh - ridersOffLow) + 1) + ridersOffLow;
		if(numRiders < ridersGettingOff)
			ridersGettingOff = numRiders;

		numRiders -= ridersGettingOff;

		return ridersGettingOff;
	}

	public int getBoardingCapacity()
	{
		return capacity - numRiders;
	}

	private int calculateNextArrivalTime(int time)
	{
		int travelTime = 0;
		double lat1,lat2,long1,long2 = 0.0;
	
		if (currentStopIndex >= (route.getStops().size() - 1 ))
		{
			lat1 = route.getStops().get(currentStopIndex).getLatitude();
			lat2 = route.getStops().get(0).getLatitude();
			long1 = route.getStops().get(currentStopIndex).getLongitude();
			long2 = route.getStops().get(0).getLongitude();
			
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
			lat1 = route.getStops().get(currentStopIndex).getLatitude();
			lat2 = route.getStops().get(currentStopIndex + 1).getLatitude();
			long1 = route.getStops().get(currentStopIndex).getLongitude();
			long2 = route.getStops().get(currentStopIndex + 1).getLongitude();
			
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
		System.out.println("b:"+id+"->s:"+route.getStops().get(currentStopIndex).getId()+"@"+arrivalTime+"//p:"+numRiders+"/f:0");
		return arrivalTime;
	}
}
