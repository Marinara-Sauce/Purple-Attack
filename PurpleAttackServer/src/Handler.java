import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Handler implements Runnable
{
	private final int TIMEOUT = 100000; //Number of ticks until a player is considered Timed Out
	private Game game; //The game that's going to be hosted
	private Socket socket;
	
	public Handler(Socket socket, Game game)
	{
		this.socket = socket;
		this.game = game;
	}
	
	@Override
	public void run() 
	{
		System.out.println("Connected: " + socket);
		int ticks = 0;
		try
		{
			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (true)
			{
				ticks++; //Increase the tick timer
				if (in.hasNextLine())
				{
					ticks = 0; //Reset the tick timer
					String input = in.nextLine();
					System.out.println("Received: " + input);
					if (input.contains("NEWPLAYER"))
					{
						String playerName = input.replace("NEWPLAYER", "");
						game.addPlayer(new Player(playerName, socket, out, in));
					}
					else if (input.equals("REQUESTCURRENTDIR"))
					{
						game.getPlayerBySocket(socket).getCurrentDirectory();
					}
					else
					{
						System.out.println("Sending Command to " + game.getPlayerBySocket(socket).getName());
						game.getPlayerBySocket(socket).processCommand(input);
					}
				}
				
				//Removes the player if they are Timedout
				if (ticks >= TIMEOUT)
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Disconnected: " + socket);
			try 
			{ 
				game.removePlayer(socket);
				socket.close(); 
			} 
			catch (Exception e) {}
		}
	}

}
