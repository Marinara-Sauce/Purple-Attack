import java.io.IOException;
import java.util.Scanner;

//Test

public class Main {

	public static void main(String[] args) throws IOException 
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to Purple Attack!");
		
		while (true)
		{
			//Starts main menu
			System.out.println("Select an Option\n1. Join a Game\n2. Play Offline (Debug)");
			
			switch (input.nextInt())
			{
				case 1:
					Game game = new Game();
					game.connect();
					break;
				case 2:
					Game game2 = new Game();
					game2.startGame();
					break;
				default:
					System.err.println("Invalid Option!");
			}
		}
	}
}
