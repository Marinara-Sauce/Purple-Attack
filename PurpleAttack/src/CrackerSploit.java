import java.util.Scanner;

public class CrackerSploit extends Website {

	public static void run()
	{
		URL = "http://www.crackersploit.onion";

		Website.run();
		
		System.out.println("Welcome to Crackersploit!\nWhy you're here, whether you need to decrypt a .sec file, decrypt a folder, or you just forgot your password, we won't judge\nWe offer cheap solutions for decrypting software.");
		Scanner input = new Scanner(System.in);
		boolean running = true;
		while (running)
		{
			switch(getInput(input, "What would you like to do?\n1. Purchase\n2. About\n3. Disconnect", 1, 3))
			{
			case 1:
				showShop();
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
	
	private static void showShop()
	{
		
	}

}
