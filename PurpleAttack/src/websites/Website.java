package websites;

import java.util.Scanner;

//The super class for all the websites
public class Website {

	protected static String TITLE = "<WEBSITE TITLE>";
	protected static String URL = "<WEBSITE URL>";
	protected static int health = 300;
	
	public static void run()
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

	public static String getTITLE() {
		return TITLE;
	}

	public static void setTITLE(String tITLE) {
		TITLE = tITLE;
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
