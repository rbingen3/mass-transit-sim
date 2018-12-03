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
	private String displayString;
	private Random randomGenerator;
	private int updatedCapacity;
	private int updatedSpeed;
	private Route updatedRoute;
	private Stop updatedNextStop;



	public Bus (int id, int initialRouteId, int currentStopIndex, int initialCapacity, int initialSpeed)
	{
		this.id = id;
		this.routeId = initialRouteId;
		this.currentStopIndex = currentStopIndex;
		this.capacity = initialCapacity;
		this.speed = initialSpeed;
		this.numRiders = 0;
		this.randomGenerator = new Random();
		this.updatedCapacity = -1;
		this.updatedSpeed = -1;
		this.updatedRoute = null;
		this.updatedNextStop = null;
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

	public String getDisplayString()
	{
		if(displayString == null || displayString.isEmpty())
		{
			displayString = "b:"+id+"->s:"+route.getStops().get(currentStopIndex).getId()+"@0//p:"+numRiders+"/f:0";
		}
		return displayString;
	}

	public void addPassengers(int num)
	{
		numRiders += num;
	}

	public int nextStopTime(int time)
	{
		return calculateNextArrivalTime(time);
	}

	public int ridersOff(int ridersGettingOff)
	{
		if(numRiders < ridersGettingOff)
			ridersGettingOff = numRiders;

		numRiders -= ridersGettingOff;

		return ridersGettingOff;
	}

	public int getBoardingCapacity()
	{
		return capacity - numRiders;
	}

	public void updateCapacity(int newCapacity)
	{
		this.updatedCapacity = newCapacity;
	}

	public void updateSpeed(int newSpeed)
	{
		this.updatedSpeed = newSpeed;
	}

	public void updateRoute(Route newRoute, Stop nextStop)
	{
		this.updatedRoute = newRoute;
		this.updatedNextStop = nextStop;
	}

	public void updateParams()
	{
		//Route changes will be handled when calculating next stop time
		if(this.updatedCapacity > -1)
		{
			this.capacity = this.updatedCapacity;
			this.updatedCapacity = -1;
		}

		if(this.updatedSpeed > -1)
		{
			this.speed = this.updatedSpeed;
			this.updatedSpeed = -1;
		}
	}

	private int calculateNextArrivalTime(int time)
	{
		double lat1,lat2,long1,long2 = 0.0;

		if(updatedRoute != null)
		{
			//calculate based on current stop and the nextstop in updated route
			lat1 = route.getStops().get(currentStopIndex).getLatitude();
			lat2 = updatedNextStop.getLatitude();
			long1 = route.getStops().get(currentStopIndex).getLongitude();
			long2 = updatedNextStop.getLongitude();

			setNextArrivalTime(time, lat1, lat2, long1, long2);

			//update the bus route
			int stopIndex = 0;
			for(int s = 0; s < route.getStops().size(); s++)
			{
				if(updatedRoute.getStops().get(s).getId() == updatedNextStop.getId())
				{
					stopIndex = s;
				}
			}
			route = updatedRoute;
			updatedRoute = null;
			updatedNextStop = null;
			currentStopIndex = stopIndex;
		}
		else
		{
			if (currentStopIndex >= (route.getStops().size() - 1 ))
			{
				lat1 = route.getStops().get(currentStopIndex).getLatitude();
				lat2 = route.getStops().get(0).getLatitude();
				long1 = route.getStops().get(currentStopIndex).getLongitude();
				long2 = route.getStops().get(0).getLongitude();

				setNextArrivalTime(time, lat1, lat2, long1, long2);
				currentStopIndex = 0;

			} else {
				lat1 = route.getStops().get(currentStopIndex).getLatitude();
				lat2 = route.getStops().get(currentStopIndex + 1).getLatitude();
				long1 = route.getStops().get(currentStopIndex).getLongitude();
				long2 = route.getStops().get(currentStopIndex + 1).getLongitude();

				setNextArrivalTime(time, lat1, lat2, long1, long2);
				currentStopIndex++;
			}
		}
		displayString = "b:"+id+"->s:"+route.getStops().get(currentStopIndex).getId()+"@"+arrivalTime+"//p:"+numRiders+"/f:0";
		System.out.println(displayString);
		return arrivalTime;
	}

	private void setNextArrivalTime(int time, double lat1, double lat2, double long1, double long2) {
		int travelTime;
		double distance = 70.0 * Math.sqrt(Math.pow((lat1 - lat2), 2) + Math.pow((long1 - long2), 2));
		travelTime = 1 + ((int) distance * 60 / speed);
		if (travelTime < 1) {
			arrivalTime = time + 1;
		} else {
			arrivalTime = time + travelTime;
		}
	}
}
