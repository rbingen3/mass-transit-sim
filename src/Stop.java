import java.util.Random;

public class Stop
{
	public int id;
	public String name;
	public int numRiders;
	private int transfers;
	public double latitude;
	public double longitude;
	private int ridersArriveHigh = 25;
	private int ridersArriveLow = 3;
	private int ridersDepartHigh = 10;
	private int ridersDepartLow = 1;
	private int ridersOnHigh = 20;
	private int ridersOnLow = 1;
	private Random randomGenerator;

	public Stop (int id, String name, int numRiders, double latitude, double longitude)
	{
		this.id = id;
		this.name = name;
		this.numRiders = numRiders;
		this.latitude = latitude;
		this.longitude = longitude;
		randomGenerator = new Random();
	}

	public int getNumRiders()
	{
		return numRiders;
	}

	public void ridersArrive()
	{
		numRiders += randomGenerator.nextInt( (ridersArriveHigh - ridersArriveLow) + 1) + ridersArriveLow;
	}

	public void ridersOn(Bus bus)
	{
		int ridersOn = randomGenerator.nextInt( (ridersOnHigh - ridersOnLow) + 1) + ridersOnLow;
		int boardingCapacity = bus.getBoardingCapacity();

		if( ridersOn > boardingCapacity)
			ridersOn = boardingCapacity;

		if(ridersOn > numRiders)
			ridersOn = numRiders;

		numRiders -= ridersOn;
		bus.addPassengers(ridersOn);
	}

	public void ridersDepart()
	{
		int ridersDepart = randomGenerator.nextInt( (ridersDepartHigh - ridersDepartLow) + 1) + ridersDepartLow;
		if(ridersDepart > (numRiders + transfers))
			ridersDepart = numRiders + transfers;

		numRiders = numRiders + transfers - ridersDepart;
		transfers = 0;
	}

	public void addTransfers(int ridersOff)
	{
		transfers += ridersOff;
	}
}
