package websites;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Goodegg implements _Website{

	private String title = "Goodegg";
	private String url = "https://www.goodegg.com";
	private int health = 1000;
	
	private String[][] catalog = {{"Intel i5 7600 - 6 Cores - $300", "Intel i7 7600 - 12 Cores - $650", "Intel i9 9600 - 24 Cores - $1200"}, {"Networking2048 - Level 2 - $125", "Connect96 - Level 3 - $275", "Online9650 - Level 4 - $500"}}; //Column 1 is cpus, colum 2 is networks
	
	@Override
	public void run() {
		
		System.out.println("Resolving DNS Host...");
		delay(2000); //This value changes depending on the users networking card
		System.out.println("Connecting to " + url);
		delay(health); //Value changes depending on if a players DDOSing the site
		System.out.println("Waiting for " + url);
		delay(health * 2);
		
		Scanner input = new Scanner(System.in);
		
		List<String> cart = new ArrayList<>();
		System.out.println("Welcome to Goodegg! The one location for all your computer hardware needs!");
		
		boolean run = true;
		while (run)
		{
			switch (getInput(input, "What would you like to do?\n1. View Catolog\n2. View Cart (" + cart.size() + " items)\n3. Disconnect", 1, 3))
			{
			case 1:
				break;
			case 2:
				break;
			case 3:
				System.out.println("Thank you for shopping at Goodegg!");
				run = false;
				break;
			}
		}
	}

	public void viewCatolog(Scanner input)
	{
		boolean run = true;
		while (run)
		{
			switch (getInput(input, "What catalog would you like to view?\n1. Processors\n2. Networking Cards\n3. Exit", 1, 2))
			{
			case 1:
				
				break;
			case 2:
				break;
			case 3:
				run = false;
				break;
			}
		}	
	}
	
	public int getInput(Scanner in, String question, int min, int max)
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
	
	@Override
	public void delay(int millis) {
		
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
