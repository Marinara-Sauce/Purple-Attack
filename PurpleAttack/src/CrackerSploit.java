import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrackerSploit extends Website {

	public static void run(Game game)
	{
		URL = "http://crackersploit.onion";

		Website.run();
		
		System.out.println("Welcome to Crackersploit!\nWhy you're here, whether you need to decrypt a .sec file, decrypt a folder, or you just forgot your password, we won't judge\nWe offer cheap solutions for decrypting software.");
		Scanner input = new Scanner(System.in);
		boolean running = true;
		while (running)
		{
			switch(getInput(input, "What would you like to do?\n1. Purchase\n2. About\n3. Disconnect", 1, 3))
			{
			case 1:
				showShop(game, input);
				break;
			case 2:
				System.out.println("What, were you expecting an sad backstory with my email address, phone number, SSN, wifes maiden name...This is a site for hackers on the deep web nice try");
				break;
			case 3:
				System.out.println("Goodbye...for now");
				running = false;
				break;
			}
		}
		
	}
	
	private static void showShop(Game game, Scanner input)
	{
		System.out.print("Fetching Version...");
		delay(1000);
		int currentVersion = game.getInventory().getDecryptor().getLevel();
		System.out.println("Done! Version: " + currentVersion);
		
		if (currentVersion == 3)
		{
			System.out.println("You already have the max version!");
			return;
		}
		
		while (true)
		{
			for (int i = currentVersion + 1; i <= 3 ; i++)
			{
				System.out.println(i + ": Version " + i + " - Cost: $" + ((i * 100) - (currentVersion * 100)) + " - Averege Speed: " + (75 - (15 * i)) + " Seconds");
			}
			System.out.println("4: Go Back");
			
			int selection = getInput(input, "What would you like to purchase?", currentVersion + 1, 4);
			
			if (selection == 4)
				return;
			
			if (selection <= currentVersion)
				System.out.println("You already own this version or are trying to downgrade!");
			else
			{
				if (purchaseItem(((selection * 100) - (currentVersion * 100)), game.getBitcoinMiner(), input))
				{
					System.out.println("Thank you for your purchase! You're my favorite customer!");
					game.getInventory().setDecryptor(new Decryptor(selection, game.getProcessor()));
					break;
				}
			}
		}
	}

}
