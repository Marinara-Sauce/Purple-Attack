import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Goodegg {

	private static String title = "Goodegg";
	private static String url = "https://www.goodegg.com";
	private static int health = 1000;
	
	private static String[][] catalog = {{"Intel i5 7600 - 6 Cores - $300", "Intel i7 7600 - 12 Cores - $650", "Intel i9 9600 - 24 Cores - $1200", "The Budget CPU - 2 Cores - $0"}, {"Networking2048 - Level 2 - $125", "Connect96 - Level 3 - $275", "Online9650 - Level 4 - $500"}}; //Column 1 is cpus, colum 2 is networks
	
	private static List<String> cart = new ArrayList<>();
	
	private static Game game;
	
	public static void run(Game _game) {
		
		game = _game;
		
		System.out.println("Resolving DNS Host...");
		delay(2000); //This value changes depending on the users networking card
		System.out.println("Connecting to " + url);
		delay(health); //Value changes depending on if a players DDOSing the site
		System.out.println("Waiting for " + url);
		delay(health * 2);
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to Goodegg! The one location for all your computer hardware needs!");
		
		boolean run = true;
		while (run)
		{
			switch (getInput(input, "What would you like to do?\n1. View Catolog\n2. View Cart (" + cart.size() + " items)\n3. Disconnect", 1, 3))
			{
			case 1:
				viewCatolog(input);
				break;
			case 2:
				viewCart(input);
				break;
			case 3:
				System.out.println("Thank you for shopping at Goodegg!");
				run = false;
				break;
			}
		}
	}

	public static void viewCatolog(Scanner input)
	{
		boolean run = true;
		while (run)
		{
			switch (getInput(input, "What catalog would you like to view?\n1. Processors\n2. Networking Cards\n3. Exit", 1, 3))
			{
			case 1:
				boolean showing = true;
				while (showing)
				{
					for (int i = 0 ; i < catalog[0].length ; i++)
						System.out.println((i + 1) + ". " + catalog[0][i]);
					
					System.out.println((catalog[0].length + 1) + ". Exit");
					int selection = getInput(input, "Enter an item to add to your cart", 1, catalog[0].length + 1);
					if (selection == catalog[0].length + 1)
						showing = false;
					else
					{
						String item = catalog[0][selection - 1];
						boolean duplicate = false;
						//Check if the user already has it in cart
						for (int i = 0 ; i < cart.size() ; i++)
						{
							if (cart.get(i).equals(item))
							{
								duplicate = true;
								System.out.println("You already have this item in cart!");
							}
						}
						
						if (!duplicate)
						{
							System.out.println(item + " added to cart");
							cart.add(item);
						}
					}
				}
				break;
			case 2:
				boolean showingNetworking = true;
				while (showingNetworking)
				{
					for (int i = 0 ; i < catalog[1].length ; i++)
						System.out.println((i + 1) + ". " + catalog[1][i]);
					
					System.out.println((catalog[1].length + 1) + ". Exit");
					int selection = getInput(input, "Enter an item to add to your cart", 1, catalog[1].length + 1);
					if (selection == catalog[1].length + 1)
						showingNetworking = false;
					else
					{
						String item = catalog[1][selection - 1];
						boolean duplicate = false;
						//Check if the user already has it in cart
						for (int i = 0 ; i < cart.size() ; i++)
						{
							if (cart.get(i).equals(item))
							{
								duplicate = true;
								System.out.println("You already have this item in cart!");
							}
						}
						
						if (!duplicate)
						{
							System.out.println(item + " added to cart");
							cart.add(item);
						}
					}
				}
				break;
			case 3:
				run = false;
				break;
			}
		}	
	}
	
	public static void viewCart(Scanner input)
	{
		if (cart.size() == 0)
		{
			System.out.println("You have no items in your cart!");
			return;
		}
		
		while (true)
		{
			System.out.println("Here are the items in your cart:");
			
			for (int i = 0 ; i < cart.size() ; i++)
				System.out.println((i + 1) + ". " + cart.get(i));
			
			switch (getInput(input, "What would you like to do?\n1. Checkout\n2. Remove Item\n3. Exit", 1, 3))
			{
			case 1:
				//Puts prices from the string into an array
				double[] prices = new double[cart.size()];
				for (int i = 0 ; i < cart.size() ; i++)
				{
					prices[i] = Double.parseDouble(cart.get(i).split("- ")[2].replace("$", ""));
				}
				
				//Calculates Total
				double total = 0;
				for (int i = 0 ; i < prices.length ; i++)
					total += prices[i];
				System.out.println("Total: $" + total + " ( BTC:" + (total / 5000) + " )");
				System.out.println("Your Wallet Has: $" + game.getBitcoinMiner().btcToUSD() + " | BTC: " + game.getBitcoinMiner().getAmount());
				
				if (getInput(input, "Would you like to purchase? ( 0 = No, 1 = Yes )", 0, 1) == 1)
				{
					if (game.getBitcoinMiner().purchase(total / 5000))
					{
						System.out.println("Purchase Successful!");
						
						for (int i = 0 ; i < cart.size() ; i++)
						{
							//Checks if processor
							if (cart.get(i).startsWith("Intel") || cart.get(i).startsWith("The"))
							{
								game.getInventory().addToInventory(new Processor(cart.get(i).split(" - ")[0], Integer.parseInt(cart.get(i).split(" - ")[1].replace(" Cores", ""))));
							}
							else
							{
								game.getInventory().addToInventory(new NetworkingCard(Integer.parseInt(cart.get(i).split(" - ")[1].replace("Level ", "")), 1500, cart.get(i).split(" - ")[0]));
							}
						}
						
						cart.clear();
						return;
					}
					else
					{
						System.out.println("You do not have enough funds for this!");
					}
				}
				break;
			case 2:
				int selection = getInput(input, "Which item would you like to remove? (Enter item number shown above, type -1 to cancel)", -1, cart.size());
				if (selection == -1)
					break;
				else if (selection == 0)
				{
					System.out.println("Invalid Option!");
					break;
				}
				else
				{
					try
					{
						cart.remove(selection - 1);
						if (cart.size() == 0)
							return;
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						System.out.println("Invalid Option!");
					}
				}
				break;
			case 3:
				return;
			}
		}
	}
	
	public static int getInput(Scanner in, String question, int min, int max)
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
	
	public static void delay(int millis) {
		
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
