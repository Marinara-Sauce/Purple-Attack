import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.Directory;

public class Game 
{
	private List<Player> players = new ArrayList<>();
	private AudioPlayer music = new AudioPlayer();
	
	//Adds a player to the game
	public void addPlayer(Player player)
	{
		//Do not allow more than 2 players
		if (players.size() > 1)
			return;
		
		players.add(player);
		startGame();
	}
	
	//Removes a player from the game
	public void removePlayer(Player player)
	{
		players.remove(player);
		//TODO: End Game
	}
	
	//Removes a player from the game based on there socket
	public void removePlayer(Socket socket)
	{
		Player playerToRemove = null;
		for (int i = 0 ; i < players.size() ; i++)
		{
			if (players.get(i).getSocket() == socket)
			{
				playerToRemove = players.get(i);
				break;
			}
		}
		System.out.println("Removing Player: " + playerToRemove.getName() + " from Game (Socket: " + playerToRemove.getSocket() + " )");
		players.remove(playerToRemove);
		//TODO: End Game
	}
	
	//Sends a message to all players
	public void broadcastToAll(String message)
	{
		for (int i = 0 ; i < players.size() ; i++)
			players.get(i).sendMessageToClient(message);
	}
	
	public void startGame()
	{
		//Check if we have 2 players
		if (!(players.size() == 2))
			return;
		
		System.out.println("Starting Game... Players length is "+ players.size());
		
		//Broadcasts the 3 second game starting warning
		broadcastToAll("GAMESTARTING");
		delay(1000);
		broadcastToAll("GAMESTARTING3");
		delay(1000);
		broadcastToAll("GAMESTARTING2");
		delay(1000);
		broadcastToAll("GAMESTARTING1");
		delay(1000);
		broadcastToAll("STARTGAME");
		
		//waitUntilMessageOnAll("FINISHEDBOOT");
		
		System.out.println("All players are ready, The game is starting...");
		
		//Set opponents name in players
		players.get(0).setOpponentName(players.get(1).getName());
		players.get(1).setOpponentName(players.get(0).getName());
		
		//Set all players IP addresses
		String player1IP = generateIP();
		String player2IP = generateIP();
		
		players.get(0).setPlayerIP(player1IP);
		players.get(1).setPlayerIP(player2IP);
		
		players.get(0).setOpponentIP(player2IP);
		players.get(1).setOpponentIP(player1IP);
		
		for (int i = 0 ; i < players.size() ; i++)
			players.get(i).setupFileSystem();

		players.get(0).setOpponentPassword(players.get(1).getPassword());
		players.get(1).setOpponentPassword(players.get(0).getPassword());
		
		broadcastToAll("OPENTERMINALS");
		delay(9000);
		
		music.playAudio("resources/Music.wav");
	}
	
	public void endGame(Player winner)
	{
		for (int i = 0 ; i < players.size() ; i++)
			players.get(i).setWinners(winner);
	}
	
	//Waits until all clients have a certain message
	public void waitUntilMessageOnAll(String message)
	{
		//Wait for Ready
		boolean ready = true;
		while (true)
		{
			for (int i = 0 ; i < players.size() ; i++)
			{
				if (!players.get(i).checkForMessage(message))
					ready = false;
			}
			if (ready)
				break;
		}
	}
	
	//Inputs the player calling this function, returns the opposing player
	public Directory getOpponentBaseDirectory(Player player)
	{
		Player otherPlayer = null;
		
		if (players.get(0) == player)
			otherPlayer = players.get(1);
		else
			otherPlayer = players.get(0);
		
		return otherPlayer.getBaseDir();
	}
	
	public void delay(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public Player getPlayerBySocket(Socket socket)
	{
		for (int i = 0 ; i < players.size() ; i++)
			if (players.get(i).getSocket() == socket)
				return players.get(i);
		
		return null;
	}
	
	public String generateIP()
	{
		return randomNumber(0, 255) + "." + randomNumber(0, 255) + "." + randomNumber(0, 255) + "." + randomNumber(0, 255);
	}
	
	public int randomNumber(int min, int max)
	{
		if (min >= max) 
		{
			System.err.println("Random Numer Min is Either Greater than or equal to the max!");
			return 0;
		}
		
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
