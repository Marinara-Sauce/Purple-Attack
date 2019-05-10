import java.util.InputMismatchException;
import java.util.Scanner;

//The super class for all the websites
public class Website {

	protected static String URL = "<WEBSITE URL>";
	protected static int health = 300;
	
	protected static void run()
	{
		System.out.println("Resolving DNS Host...");
		delay(2000); //This value changes depending on the users networking card
		System.out.println("Connecting to " + URL);
		delay(health); //Value changes depending on if a players DDOSing the site
		System.out.println("Waiting for " + URL);
		delay(health * 2);
		
		Scanner input = new Scanner(System.in);
		//The classes run the code here
	}
	
	protected static int getInput(Scanner in, String question, int min, int max)
	{
		int input;
		
		while (true)
		{
			System.out.println(question);
			try
			{
				input = in.nextInt();
				
				if (input >= min && input <= max)
					break;
				
				System.err.println("Invalid Option!");
			}
			catch (InputMismatchException e)
			{
				System.err.println("Please enter an number!");
			}
		}
		
		return input;
	}
	
	protected static void delay(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
