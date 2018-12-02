
public class Main 
{

	public static void main(String[] args) 
	{
		if(args.length < 1){
			System.out.println("Configuration file not specified, exiting simulation.");
			System.exit(0);
		}

		Simulation sim = new Simulation(args[0],20);
	}

}
