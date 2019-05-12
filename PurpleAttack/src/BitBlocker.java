import java.util.Scanner;

public class BitBlocker extends Website {

	public BitBlocker() 
	{
		URL = "https://www.bitblock.com";
		health = 3000;
	}

	public static void run(Game game)
	{
		Website.run();
		
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to BitBlock! The one stop for all your defense against the dark hacking-art needs!");
		System.out.println("We offer various products for defense and security, such as the Firewall, Connection Blocker, and Malewarebits");
		
		boolean running = true;
		
		while (running)
		{
			switch (getInput(input, "Please choose a product:\n1. Firewall\n2. Connection Blocker\n3. Malewarebits\n4. Disconnect", 1, 4))
			{
			case 1:
				openFirewallShop(game, input);
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				System.out.println("Thank you! We hope you connect again soon!");
				running = false;
				break;
			}
		}
	}
	
	private static void openFirewallShop(Game game, Scanner input)
	{
		int currentVersion = game.getInventory().getFirewall().getLevel();
		
		System.out.println("Checking Current Firewall Version...");
		delay(1000);
		System.out.println("Found Version: " + currentVersion);
		
		System.out.println("");
		
		boolean running = true;
		
		final String[] catalog = {"DEFAULT_VERSION", "V2 - Chance to Block: 15% + 2% Each Core", "V3 - Chance to Block: 30% + 2% Each Core", "V4 - Chance to Block: 60% (Constant, dosen't change with core count)"};
		
		while (running)
		{
			System.out.println("Choose A Version to Upgrade To:");
			
			for (int i = currentVersion - 1 ; i < catalog.length ; i++)
			{
				if (i > currentVersion - 1)
				{
					System.out.print((i + 1) + ". " + catalog[i] + " - Price: $" + calculatePrice(i + 1, currentVersion));
					System.out.println("");
				}
			}
			
			System.out.print("5. Go Back");
			int selection = getInput(input, "", currentVersion, 5);
			
			if (selection == 5)
				return;
			
			if (selection <= currentVersion)
			{
				System.out.println("You already have this version, or you are trying to downgrade.");
			}
			else
			{
				int price = calculatePrice(selection, currentVersion);
				
				if (purchaseItem(price, game.getBitcoinMiner(), input))
				{
					System.out.println("Thank you for securing with BitBlocker!");
					game.getInventory().setFirewall(new Firewall(selection, game.getProcessor(), game));
				}
				else
				{
					return;
				}
			}
		}
	}
	
	private static void openMalewarebitsShop()
	{
		//TODO: Maleware and Stuff
	}
	
	private static void openConnectionBlockerShop()
	{
		
	}
	
	private static int calculatePrice(int version, int currentVersion)
	{
		return (version * 250) - (currentVersion * 250);
	}
	
}
