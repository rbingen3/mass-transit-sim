import java.util.Random;

public class Stop
{
	private int id;
	private String name;
	private int numRiders;
	private int transfers;
	private double latitude;
	private double longitude;
	private int ridersArriveHigh;
	private int ridersArriveLow;
	private int ridersDepartHigh;
	private int ridersDepartLow;
	private int ridersOnHigh;
	private int ridersOnLow;
	private Random randomGenerator;

	public Stop (int id, String name, int numRiders, double latitude, double longitude)
	{
		this.id = id;
		this.name = name;
		this.numRiders = numRiders;
		this.latitude = latitude;
		this.longitude = longitude;
		this.ridersArriveHigh = 25;
		this.ridersArriveLow = 3;
		this.ridersDepartLow = 1;
		this.ridersDepartHigh = 10;
		this.ridersOnHigh = 20;
		this.ridersOnLow = 1;
		this.randomGenerator = new Random();
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public int getNumRiders()
	{
		return numRiders;
	}

	public void ridersArrive()
	{
		numRiders += randomGenerator.nextInt((ridersArriveHigh - ridersArriveLow) + 1) + ridersArriveLow;
	}

	public void ridersOn(Bus bus)
	{
		int ridersOn = randomGenerator.nextInt((ridersOnHigh - ridersOnLow) + 1) + ridersOnLow;
		int boardingCapacity = bus.getBoardingCapacity();

		if(ridersOn > boardingCapacity)
			ridersOn = boardingCapacity;

		if(ridersOn > numRiders)
			ridersOn = numRiders;

		numRiders -= ridersOn;
		bus.addPassengers(ridersOn);
	}

	public void ridersDepart()
	{
		int ridersDepart = randomGenerator.nextInt((ridersDepartHigh - ridersDepartLow) + 1) + ridersDepartLow;
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
