import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException 
	{	
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to Purple Attack!");
		System.out.println("This game looks better in fullscreen! Press F11 to Enter Fullscreen Mode!");
		
		setWindowTitle("Purple Attack");
		
		while (true)
		{
			try
			{
				//Starts main menu
				System.out.println("Select an Option\n1. Join a Game\n2. Exit");
				
				switch (input.nextInt())
				{
					case 1:
						Game game = new Game();
						game.connect();
						setWindowTitle("Purple Attack");
						break;
					case 2:
						System.out.println("Goodbye! Thanks for playing Purple Attack!");
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.exit(0);
					default:
						System.err.println("Invalid Option!");
				}
			}
			catch (InputMismatchException e)
			{
				System.out.println("Invalid Input!");
				input.nextLine();
			}
			
		}
	}
	
	private static void setWindowTitle(String title)
	{
		//Sets the window title
		try {
			new ProcessBuilder("cmd", "/c", "title " + title).inheritIO().start().waitFor();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
