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
	private int ridersOffHigh;
	private int ridersOffLow;
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
		this.ridersOffLow = 1;
		this.ridersOffHigh = 20;
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

	public void setRidersArriveHigh(int ridersArriveHigh)
	{
		this.ridersArriveHigh = ridersArriveHigh;
	}

	public void setRidersArriveLow(int ridersArriveLow)
	{
		this.ridersArriveLow = ridersArriveLow;
	}

	public void setRidersOffHigh(int ridersOffHigh)
	{
		this.ridersOffHigh = ridersOffHigh;
	}

	public void setRidersOffLow(int ridersOffLow)
	{
		this.ridersOffLow = ridersOffLow;
	}

	public void setRidersOnHigh(int ridersOnHigh)
	{
		this.ridersOnHigh = ridersOnHigh;
	}

	public void setRidersOnLow(int ridersOnLow)
	{
		this.ridersOnLow = ridersOnLow;
	}

	public void setRidersDepartHigh(int ridersDepartHigh)
	{
		this.ridersDepartHigh = ridersDepartHigh;
	}

	public void setRidersDepartLow(int ridersDepartLow)
	{
		this.ridersDepartLow = ridersDepartLow;
	}
	
	public int getRidersArriveHigh() {
		return ridersArriveHigh;
	}

	public int getRidersArriveLow() {
		return ridersArriveLow;
	}

	public int getRidersDepartHigh() {
		return ridersDepartHigh;
	}

	public int getRidersDepartLow() {
		return ridersDepartLow;
	}

	public int getRidersOnHigh() {
		return ridersOnHigh;
	}

	public int getRidersOnLow() {
		return ridersOnLow;
	}

	public int getRidersOffHigh() {
		return ridersOffHigh;
	}

	public int getRidersOffLow() {
		return ridersOffLow;
	}

	public void ridersArrive()
	{
		numRiders += randomGenerator.nextInt((ridersArriveHigh - ridersArriveLow) + 1) + ridersArriveLow;
	}

	public int ridersOff(Bus bus)
	{
		int ridersGettingOff = randomGenerator.nextInt((ridersOffHigh - ridersOffLow) + 1) + ridersOffLow;
		return bus.ridersOff(ridersGettingOff);
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
