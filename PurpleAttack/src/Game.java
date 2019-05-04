import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import commands.Help;

//This class runs the game. This controls the terminal, commands, and communicating with the server

public class Game 
{
	private String name; //The players name
	
	private Scanner input; //Main Input Scanner for Player
	private Scanner in; //Input for network
	private PrintWriter out; //Output for network
	private Socket socket;
	
	private List<String> bootSequenceText;
	
	private Inventory inventory;
	
	private BitcoinMiner bitcoin;
	
	private boolean gameOver;
	
	public Game()
	{
		input = new Scanner(System.in);
		bootSequenceText = getBootSequenceText();
	}
	
	public void setPlayerName()
	{
		if (name != null && !name.isEmpty())
			return;
		System.out.print("Enter an Alias > ");
		String potentialName = input.nextLine();
		if (!potentialName.isEmpty())
		{
			name = potentialName;
			try {
				new ProcessBuilder("cmd", "/c", "title Purple Attack: Playing As: " + name).inheritIO().start().waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("Your name cannot be empty!");
			setPlayerName();
		}
	}
	
	public void connect() throws IOException
	{
		setPlayerName();
		System.out.println("Enter the Server IP (Leave Blank for Localhost)\nType -1 to Go Back");
		while (true)
		{
			String selection = input.nextLine();
			
			if (selection.equals("-1")) return;
			else if (selection.isEmpty()) selection = "localhost";
			
			try
			{
				System.out.print("Connecting...");
				socket = new Socket(selection, 59989);
				System.out.println(" Successs! ");
				initConnection();
				
				//After game ends
				socket.close();
				out.close();
				in.close();
				break;
			}
			catch (Exception e)
			{
				System.out.println("");
				System.err.println("Cannot connect to the server!");
				delay(100);
				connect();
			}
		}
		
	}
	
	//This function inits variables needed in order to communicate to and from the server
	public void initConnection() throws IOException
	{
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream(), true);
		
		//Tells the server the nessecarry info to make a new game
		out.println("NEWPLAYER" + name);
		
		System.out.println("Waiting for 1 more player...");
		
		while (true)
		{
			String message = in.nextLine();
			
			//Game Starting Warning
			if (message.equals("GAMESTARTING"))
				System.out.println("Game Starting...");
			
			else if (message.equals("GAMESTARTING3"))
				System.out.println("Game Starting...3");
			
			else if (message.equals("GAMESTARTING2"))
				System.out.println("Game Starting...2");
			
			else if (message.equals("GAMESTARTING1"))
				System.out.println("Game Starting...1");
			
			else if (message.equals("STARTGAME"))
			{
				startGame();
				break;
			}
		}
	}
	
	public void startGame()
	{
		gameOver = false;
	
		playBootSequence();
		
		inventory = new Inventory();
		
		//Inits Bitcoin and such
		bitcoin = new BitcoinMiner(this, inventory.getEquippedProcessor());
		bitcoin.startMining();
		
		inventory.getEquippedProcessor().setBitcoinMiner(bitcoin);
		
		System.out.println("Begin entering commands. Type help for options");
		
		//This is where commands run and are processed
		while(!gameOver)
		{
			String command = getInput();
			
			//Sends commands to the bitcoin class
			if (command.contains("bitcoin"))
				bitcoin.processCommand(command.replace("bitcoin ", ""));
			
			else if (command.startsWith("processor"))
				inventory.getEquippedProcessor().processCommand(command.replace("processor ", ""));
			
			else
			{
				if (command.equals("help"))
					Help.run();
				
				else if (command.equals("clr") || command.equals("clear"))
					clearConsole();
				
				else if (command.equals("dir") || command.equals("ls"))
					printDirs(command);
				
				else if (command.contains("cat"))
					cat(command);
				
				else if (command.contains("mkdir"))
					makeDir(command);
				
				else if (command.contains("cd"))
					changeDir(command);
				
				else if (command.contains("rmdir"))
					removeDir(command);
				
				else if (command.contains("create"))
					createFile(command);
				
				else if (command.contains("mv"))
					move(command);
				
				else if (command.contains("cp"))
					copy(command);
				
				else if (command.contains("rm"))
					remove(command);
				
				else if (command.contains("ps"))
					paste(command);
				
				else if (command.contains("connect") && !command.equalsIgnoreCase("disconnect"))
					connect(command);
				
				else if (command.equalsIgnoreCase("disconnect"))
					disconnect(command);
				
				else if (command.contains("echo"))
					System.out.println(command.replace("echo ", ""));
				
				else if (command.startsWith("inventory"))
					inventory.processCommand(command);
				
				else if (command.startsWith("password"))
					passwordDialog(command);
				
				else if (command.startsWith("EndGame"))
					endGame(true);
				
				else
					System.out.println("Unknown command! Type help for a list of commands");
			}
			bitcoin.updateBitcoinAmount();
		}
		//Game Ended at this point
		clearConsole();
		return;
	}
	
	//Gets the results of the command from the server. Called from within commands.
	public boolean getCommandSuccess()
	{
		while (input.hasNextLine())
		{
			String received = in.nextLine();
			
			if (received.equals("SUCCESS"))
				return true;
			
			else if (received.contains("FAILURE"))
				return false;
		}
		return false;
	}
	
	//Gets the results of the command from the server using custom failure and success messages. Called from within commands.
	public boolean getCommandSuccess(String success, String failed)
	{
		int tick = 0;
		
		while (in.hasNextLine())
		{
			String received = in.nextLine();
			
			if (received.equals(success))
				return true;
			
			else if (received.equals(failed))
				return false;
			
			tick++;
			if (tick > 1000)
			{
				System.out.println("Timed out waiting for responce from server");
				return false;
			}
		}
		return false;
	}
	
	//Plays the boot sequence
	public void playBootSequence()
	{
		clearConsole();
		
		for (int i = 0 ; i < bootSequenceText.size() ; i++)
		{
			String text = bootSequenceText.get(i);
			if (text.equals("Done!") || text.equals("Located!") || text.equals("Finished!"))
				delay(500);
			
			System.out.println(text);
			delay(50);
		}
		
		System.out.print("Finializing Boot...");
		
		try
		{
			out.println("FINISHEDBOOT");
			waitForMessage("OPENTERMINALS");
		}
		catch (Exception e)
		{
			System.err.println("No Server Found!");
		}
		
		System.out.println("Finished!");
		delay(500);
		
		clearConsole();
	}
	
	//Called by the client, sends the selected path to the client
	public String getInput()
	{
		out.println("REQUESTCURRENTDIR");
		delay(50);
		
		while (in.hasNextLine())
		{
			String line = in.nextLine();
			
			if (line.startsWith("GAMEOVER"))
			{
				//Run Game Over Dialog
				String result = line.replace("GAMEOVER", "");
				
				if (result.equals("WIN")) 
				{
					endGame(true);
					return "";
				}
				
				else if (result.equals("LOSE"))
				{
					endGame(true);
					return "";
				}
				
				else System.err.println("Unknown Win Condition: " + result);
			}
			
			else if (!line.isEmpty() && !line.equals("CDFAILED")) 
			{
				System.out.print(line);
				break;
			}
		}
		
		System.out.print(" > ");
		return input.nextLine();
	}
	
	public void endGame(boolean won)
	{
		gameOver = true;
		if (won)
		{
			clearConsole();
			animatedPrint("Congradulations");
			delay(1000);
			animatedPrint("We used the password to look into the database. It worked.");
			delay(1000);
			animatedPrint("Thanks to you, we were able to confirm that they have indeed stole our cirriculumn. We contacted Mr. Atsoc to take legal action.");
			delay(1000);
			animatedPrint("Good work out there. You saved AP Computer Science A");
			delay(1000);
			animatedPrint("-Eric");
			delay(1000);
			System.out.println("");
			delay(5000);
			
			//Load the victory screen
			List<String> vicScreen = getVictoryScreenText();
			
			for (int i = 0 ; i < vicScreen.size() ; i++)
				System.out.println(vicScreen.get(i));
		}
		else
		{
			clearConsole();
			animatedPrint("You Lost");
			delay(1000);
			animatedPrint("The opponent stole our password, they now have access to our cirriculumn!");
			delay(1000);
			animatedPrint("You failed to please me, you are now doomed to 50s for the rest of the semester.");
			delay(1000);
			animatedPrint("Goodbye");
			delay(1000);
			animatedPrint("-Eric");
			delay(5000);
			System.out.println("");
		}
		
		System.out.println("Press Enter to Return to Main Menu");
		input.nextLine();
	}
	
	//Prints the string one character at a time. Used in endGame
	public void animatedPrint(String line)
	{
		for (int i = 0 ; i < line.length() ; i++)
		{
			System.out.print(line.charAt(i));
			delay(25);
		}
		
		System.out.println("");
	}
	
	//-------------------------COMMAND FUNCTIONS---------------------------//
	
	public void passwordDialog(String command)
	{
		String password;
		
		if (command.length() == "password".length())
		{
			System.out.println("Enter a password to submit");
			password = input.nextLine();
		}
		else
			password = command.replace("password ", "");
		
		System.out.println("Checking password: " + password);
		out.println("PASSWORD" + password);
		
		delay(50);
		
		while (in.hasNextLine())
		{
			String line = in.nextLine();
			if (line.equals("PASSWORDCORRECT"))
			{
				System.out.println("Access Granted!");
				delay(2500);
				break;
			}
			else if (line.equals("PASSWORDINCORRECT"))
			{
				System.out.println("Access Denied!");
				delay(2500);
				break;
			}
		}
	}
	
	public void connect(String command)
	{
		String ip;
		
		if (command.length() == "connect".length())
		{
			System.out.println("Please enter the IP or website to connect to (Example: 192.168.1.1)");
			ip = input.nextLine();
		}
		else
			ip = command.replace("connect ", "");
		
		final String[] websites = {"www.goodegg.com"};
		
		//Check for and connect to any websites
		
		boolean website = false;
		
		for (int i = 0 ; i < websites.length ; i++)
		{
			if (ip.equals(websites[i]))
			{
				website = true;
				
				//Connects
				if (ip.equals("www.goodegg.com"))
					Goodegg.run(this);
			}
		}
		
		if (!website)
		{
			out.println("CONNECT" + ip);
			System.out.println("Connecting to " + ip + "...");
			delay(getNetworkingCard().getUpdate() + 500);
			
			while (in.hasNextLine())
			{
				String line = in.nextLine();
				
				if (line.equals("CONNECTSUCCESS"))
				{
					System.out.println("Success!");
					break;
				}
				else if (line.equals("CONNECTFAILEDALLREADYCONNECTED"))
				{
					System.out.println("Failed to connect! Terminate the current connection to connect");
					break;
				}
				else if (line.equals("CONNECTFAILEDCONNECTTOSELF"))
				{
					System.out.println("Failed to connect! You cannot connect to yourself!");
					break;
				}
				else if (line.equals("CONNECTFAILEDWRONGIP"))
				{
					System.out.println("Failed to connect! Could not locate IP");
					break;
				}
				else
				{
					break;
				}
			}
		}
	}
	
	public void disconnect(String command)
	{
		out.println("DISCONNECT");
		
		delay(50);
		
		while (in.hasNextLine())
		{
			String line = in.nextLine();
			
			if (line.equals("DISCONNECTSUCCESS"))
			{
				break;
			}
			else if (line.equals("DISCONNECTFAILED"))
			{
				System.out.println("Failed to disconnect, you are not connected to a server!");
				break;
			}
		}
	}
	
	public void printDirs(String command)
	{
		out.println(command);
		//Prints the results from the server
		while (in.hasNextLine())
		{
			String dirIn = in.nextLine();
			
			if (dirIn.equals("ENDOFLINE"))
				break;
			
			System.out.println(dirIn);
		}
		System.out.println("----------------------------------");
	}
	
	public void cat(String command)
	{
		String fileName;
		
		if (command.length() == "cat".length())
		{
			System.out.println("Enter a text file to view (Example: TextDoc.txt)");
			fileName = input.nextLine();
		}
		else
			fileName = command.replace("cat ", "");
		
		out.println("CAT" + fileName);
		
		while (in.hasNextLine())
		{
			String catIn = in.nextLine();
			
			if (catIn.equals("ENDOFLINE"))
				break;
			else if (catIn.equals("FAILURE"))
			{
				System.out.println("Could not find file!");
				break;
			}
			
			System.out.println(catIn);
		}
		System.out.println("---------------------------------");
	}
	
	public void createFile(String command)
	{
		String fileName;
		
		if (!command.equalsIgnoreCase("create"))
			fileName = command.replace("create " , "");
		
		else
		{
			System.out.println("Enter the file name");
			fileName = input.nextLine();
		}
		
		System.out.println("Please enter the file's contents. Type `6 for a new line. Press enter to save.");
		String contents = input.nextLine().replace("`6", "\n");
		
		out.println("NEWTEXT" + fileName + ":" + contents);
		
	}
	
	public void paste(String command)
	{
		out.println("PASTE");
		
		while (in.hasNextLine())
		{
			String line = in.nextLine();
			
			if (line.equals("SUCCESS"))
			{
				System.out.println("File Pasted!");
				break;
			}
			else if (line.equals("FAILED"))
			{
				System.out.println("The clipboard is empty!");
				break;
			}
		}
	}
	
	public void move(String command)
	{
		String fileName;
		if (command.length() == "mv".length())
		{
			System.out.println("Enter the name of the file or directory to move");
			fileName = input.nextLine();
		}
		else
		{
			fileName = command.replace("mv ", "");
		}
		
		out.println("MV" + fileName);
		delay(100);
		
		while (in.hasNextLine())
		{
			String line = in.nextLine();
			
			if (line.equals("SUCCESS"))
			{
				System.out.println("File copied to clipboard!");
				break;
			}
			else if (line.equals("FAILED"))
			{
				System.out.println("Could not find file: " + fileName);
				break;
			}
			else if (line.equals("PROTECTED"))
			{
				System.out.println("Failed to move! Access Denied!");
				break;
			}
		}
	}
	
	public void copy(String command)
	{
		String fileName;
		if (command.length() == "cp".length())
		{
			System.out.println("Enter the name of the file or directory to copy");
			fileName = input.nextLine();
		}
		else
		{
			fileName = command.replace("cp ", "");
		}
		
		out.println("CP" + fileName);
		delay(100);
		
		while (in.hasNextLine())
		{
			String line = in.nextLine();
			
			if (line.equals("SUCCESS"))
			{
				System.out.println("File copied to clipboard!");
				break;
			}
			else if (line.equals("FAILED"))
			{
				System.out.println("Could not find file: " + fileName);
				break;
			}
			else if (line.equals("PROTECTED"))
			{
				System.out.println("B What did you do wrong");
				break;
			}
		}
	}
	
	public void remove(String command)
	{
		String fileName;
		if (command.length() == "remove".length())
		{
			System.out.println("Enter the name of the file to remove");
			fileName = input.nextLine();
		}
		else
		{
			fileName = command.replace("rm ", "");
		}
		
		out.println("REMOVE" + fileName);
		
		delay(100);
		
		while (in.hasNextLine())
		{
			String result = in.nextLine();
			if (result.equals("SUCCESS"))
			{
				System.out.println("Removed file " + fileName + " successfully!");
				break;
			}
			else if (result.equals("FAILED"))
			{
				System.out.println("Failed to remove file " + fileName + ". File does not exsist!");
				break;
			}
			else if (result.equals("PROTECTED"))
			{
				System.out.println("Failed to remove directory! Access denied!");
				break;
			}
		}
	}
	
	public void makeDir(String command)
	{
		String dirName;
		if (command.length() == "mkdir".length())
		{
			System.out.println("Enter a name for the new directory");
			dirName = input.nextLine();
		}
		else
		{
			dirName = command.replace("mkdir ", "");
		}
		
		out.println("MKDIR"+dirName);
		System.out.println("Created Directory " + dirName + " Sucessfully!");
	}
	
	public void changeDir(String command)
	{
		String dir = command.replace("cd ", "");
		String password = null;
		
		if (dir.contains(" -"))
		{
			password = dir.split(" -")[1];
			dir = dir.split(" -")[0];
		}
		
		if (password == null) out.println("CD" + dir);
		else out.println("CD"+dir+":"+password);
		
		delay(100);
		
		while (in.hasNextLine())
		{
			String received = in.nextLine();
			
			if (received.equals("SUCCESS"))
				break;
			else if (received.equals("PASSWORDFAILED"))
			{
				if (password != null) System.out.println("The password is incorrect");
				else System.out.println("This directory is locked with a password. Please pass in the password using cd " + dir + " -<Password>");
				break;
			}
			else if (received.equals("CDFAILED"))
			{
				System.out.println("Could not find directory, " + dir);
				break;
			}
		}
	}
	
	public void removeDir(String command)
	{
		String dir;
		if (command.length() > "rmdir".length())
		{
			//This runs if there are more characters and therefore a directory is already passed
			dir = command.replace("rmdir ", "");
		}
		else
		{
			System.out.println("Please enter a directory to remove");
			dir = input.nextLine();
		}
		
		out.println("RMDIR" + dir);
		
		delay(100);
		
		while (in.hasNextLine())
		{
			String inLine = in.nextLine();
			
			if (inLine.equals("SUCCESS"))
			{
				System.out.println("Removed directory " + dir);
				break;
			}
			else if (inLine.equals("FAILED"))
			{
				System.out.println("Could not find directory " + dir);
				break;
			}
			else if (inLine.equals("PROTECTED"))
			{
				System.out.println("Failed to remove! Access Denied!");
				break;
			}
		}
	}
	
	//--------------------------UTIL FUNCTIONS-----------------------//
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public NetworkingCard getNetworkingCard()
	{
		return inventory.getEquippedNetworkingCard();
	}
	
	public Processor getProcessor()
	{
		return inventory.getEquippedProcessor();
	}
	
	public BitcoinMiner getBitcoinMiner()
	{
		return bitcoin;
	}
	
	public void delay(int time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Clears the Console
	public void clearConsole()
	{
		try
		{
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	//Waits until the client receives a message from the server
	public void waitForMessage(String message)
	{
		while (true)
		{
			String mes = in.nextLine();
			
			if (mes.equals(message))
				break;
		}
	}
	
	//Gets all the text for the boot sequence
	private List<String> getBootSequenceText()
	{
		List<String> text = new ArrayList<String>();
		File inFile = new File("BootSequence.txt");
		try 
		{
			Scanner textInput = new Scanner(inFile);
			while (textInput.hasNextLine())
			{
				text.add(textInput.nextLine());
			}
			//textInput.close();
			
			return text;
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("Could not locate BootSequence.txt!");
			System.exit(1);
		}
		
		return null;
	}
	
	private List<String> getVictoryScreenText()
	{
		List<String> text = new ArrayList<String>();
		File inFile = new File("VictoryScreen.txt");
		try
		{
			Scanner textInput = new Scanner(inFile);
			while (textInput.hasNextLine())
			{
				text.add(textInput.nextLine());
			}
			
			return text;
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Could not locate VictoryScreen.txt!");
			System.exit(1);
		}
		
		return null;
	}
}
