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
	
	//A function that starts the purchase process, returns true if purchased
	public static boolean purchaseItem(int price, BitcoinMiner btc, Scanner input)
	{
		System.out.println("Price: $" + price + "( BTC: " + (price / 5000) + " )");
		System.out.println("Your Wallet's Balance: $" + btc.btcToUSD() + " ( BTC: " + btc.getAmount() + " )");
		System.out.println();
		
		int selection = getInput(input, "Would you like to purchase? (0 = No, 1 = Yes)", 0, 1);
		
		if (selection == 0)
			return false;
		
		if (selection == 1)
		{
			if (btc.purchase(price / 5000))
			{
				System.out.println("Transaction Success!");
				return true;
			}
			else
			{
				System.out.println("Transaction Failed! Insufficeint Funds!");
				return false;
			}
		}
		
		return false;
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
