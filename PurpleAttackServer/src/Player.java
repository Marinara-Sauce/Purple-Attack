import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import game.Directory;
import game.File;
import game.SecureFile;
import game.TextFile;

public class Player 
{
	private Game game;
	
	private String name;
	private Socket socket;
	private PrintWriter out;
	private Scanner in;
	
	private String opponentName;
	
	private List<Directory> homeDir = new ArrayList<>();
	private List<File> homeFiles = new ArrayList<>();
	
	private Directory baseDir;
	
	private Directory currentDir;
	
	//Variables for the clipboard
	private Directory copiedDirectory;
	private File copiedFile;
	private boolean moveFile;
	
	private String playerIP;
	private String opponentIP;
	
	private String secureFolderPassword = "ligma";
	private String password = "memes";
	
	private boolean connectedToOpponent; //True if the player is connected to another player
	private Directory opponentCurrentDir; //Current Directory but if connected to someone else
	
	public Player(String name, Socket socket, PrintWriter out, Scanner in, Game game)
	{
		this.name = name;
		this.socket = socket;
		this.out = out;
		this.in = in;
		this.game = game;
		
		System.out.println("Created a new player with name: " + name + " | Socket: " + socket);
	}
	
	//Runs code for a specific player based on a command from the client
	public void processCommand(String command)
	{
		System.out.println("Received Command from " + name + " | " + command);
		
		if (command.equals("dir"))
			currentDir.displayDirectory(out);
		
		if (command.contains("CAT"))
		{
			System.out.println("Running CAT Command for " + name);
			
			List<File> files = currentDir.getFiles();
			
			String fileName = command.replace("CAT", "");
			
			boolean foundFile = false;
			for (int i = 0 ; i < files.size() ; i++)
			{
				if (files.get(i) instanceof TextFile && files.get(i).getName().equals(fileName))
				{
					foundFile = true;
					TextFile tFile = (TextFile)(files.get(i));
					tFile.readContents(out);
				}
					
			}
			
			if (foundFile)
				out.println("ENDOFLINE");
			else
				out.println("FAILURE");
			
			System.out.println("Finished CAT Command");
		}
		
		if (command.contains("MKDIR"))
		{
			System.out.println("Running MKDIR Command for " + name);
			String dirName = command.replace("MKDIR", "");
			currentDir.addDirectory(dirName);
			System.out.println("Directory " + dirName + " Successfully Created!");
		}
		
		if (command.contains("CD"))
		{
			System.out.println("Running CD Command for " + name);
			String dir = command.replace("CD", "");
			String password = null;
			
			if (dir.contains(":"))
			{
				//Gets the passed in password
				password = dir.split(":")[1];
				dir = dir.split(":")[0];
			}
			
			Directory dirToChange = null;
			
			if (dir.equals("..") && currentDir.getParent() != null)
			{
				currentDir = currentDir.getParent();
				out.println("SUCCESS");
				return;
			}
			
			for (int i = 0 ; i < currentDir.getDirectorys().size() ; i++)
			{
				if (currentDir.getDirectorys().get(i).getName().equals(dir))
				{
					dirToChange = currentDir.getDirectorys().get(i);
					
					if (dirToChange.getPassword() != null)
					{
						if (password != null && password.equals(dirToChange.getPassword()))
						{
							currentDir = dirToChange;
							out.println("SUCCESS");
							return;
						}
						else
						{
							out.println("PASSWORDFAILED");
							return;
						}
					}
					else
					{
						currentDir = dirToChange;
						out.println("SUCCESS");
					}
				}
			}
			
			out.println("CDFAILED");
		}
		
		if (command.contains("RMDIR"))
		{
			System.out.println("Running RMDIR Command for " + name);
			
			String dir = command.replace("RMDIR", "");
			
			boolean foundDirectory = false;
			for (int i = 0 ; i < currentDir.getDirectorys().size() ; i++)
			{
				if (currentDir.getDirectorys().get(i).getName().equals(dir))
				{
					foundDirectory = true;
					currentDir.getDirectorys().remove(currentDir.getDirectorys().get(i));
				}
			}
			
			if (foundDirectory)
			{
				out.println("SUCCESS");
				System.out.println("Success!");
			}
			else
			{
				out.println("FAILED");
				System.out.println("Failed!");
			}
		}
		
		if (command.contains("NEWTEXT"))
		{
			//The string will be passed in as NEWTEXTName:TextInfo
			String commandIn = command.replace("NEWTEXT", "");
			String fileInfo = commandIn.split(":")[1];
			
			TextFile newFile = new TextFile(commandIn.split(":")[0]);
			newFile.setContents(fileInfo);
			
			currentDir.addFile(newFile);
		}
		
		if (command.contains("REMOVE"))
		{
			String fileName = command.replace("REMOVE", "");
			boolean success = false;
			
			for (int i = 0 ; i < currentDir.getFiles().size() ; i++)
			{
				if (currentDir.getFiles().get(i).getName().equals(fileName))
				{
					currentDir.getFiles().remove(currentDir.getFiles().get(i));
					success = true;
				}
			}
			
			if (success)
				out.println("SUCCESS");
			else
				out.println("FAILED");
		}
		
		if (command.contains("CP"))
		{
			String fileName = command.replace("CP", "");
			boolean success = false;
			
			for (int i = 0 ; i < currentDir.getFiles().size() ; i++)
			{
				if (currentDir.getFiles().get(i).getName().equals(fileName))
				{
					clipboard(currentDir.getFiles().get(i), false);
					success = true;
				}
			}
			
			if (!success)
			{
				for (int i = 0 ; i < currentDir.getDirectorys().size() ; i++)
				{
					if (currentDir.getDirectorys().get(i).getName().equals(fileName))
					{
						clipboard(currentDir.getDirectorys().get(i), false);
						success = true;
					}
				}
			}
			
			if (success)
				out.println("SUCCESS");
			else
				out.println("FAILED");
		}
		
		if (command.contains("MV"))
		{
			String fileName = command.replace("MV", "");
			boolean success = false;
			
			for (int i = 0 ; i < currentDir.getFiles().size() ; i++)
			{
				if (currentDir.getFiles().get(i).getName().equals(fileName))
				{
					clipboard(currentDir.getFiles().get(i), true);
					success = true;
				}
			}
			
			if (!success)
			{
				for (int i = 0 ; i < currentDir.getDirectorys().size() ; i++)
				{
					if (currentDir.getDirectorys().get(i).getName().equals(fileName))
					{
						clipboard(currentDir.getDirectorys().get(i), true);
						success = true;
					}
				}
			}
			
			if (success)
				out.println("SUCCESS");
			else
				out.println("FAILED");
		}
		
		if (command.contains("PASTE"))
		{
			if (copiedDirectory == null && copiedFile == null)
			{
				out.println("FAILED");
				return;
			}
			
			if (copiedDirectory == null)
			{
				currentDir.addFile(copiedFile);
				
				if (moveFile)
					copiedFile = null;
				
				out.println("SUCCESS");
			}
			else if (copiedFile == null)
			{
				currentDir.addDirectory(copiedDirectory);
				
				if (moveFile)
					copiedDirectory = null;
				
				out.println("SUCCESS");
			}
		}
		
		if (command.equals("DISCONNECT"))
		{
			if (!connectedToOpponent)
			{
				out.println("DISCONNECTFAILED");
				return;
			}
			
			connectedToOpponent = false;
			currentDir = baseDir;
			System.out.println("Attempting to Disconnect");
			out.println("DISCONNECTSUCCESS");
			
			return;
		}
		
		if (command.contains("CONNECT"))
		{
			if (connectedToOpponent)
			{
				out.println("CONNECTFAILEDALLREADYCONNECTED");
				return;
			}
			
			String ip = command.replace("CONNECT", "");

			//Checks if the ip is a website name or ip
			if (ip.contains("."))
			{
				if (ip.equals(playerIP))
				{
					out.println("CONNECTFAILEDCONNECTTOSELF");
					return;
				}
				else if (ip.equals(opponentIP))
				{
					out.println("CONNECTSUCCESS");
					connectedToOpponent = true;
					opponentCurrentDir = game.getOpponentBaseDirectory(this);
					currentDir = opponentCurrentDir;
					return;
				}
				else
				{
					out.println("CONNECTFAILEDWRONGIP");
					return;
				}
			}
		}
	}
	
	//Sets up a file system for the game
	public void setupFileSystem()
	{
		//Sets the password variables to something random
		
		List<String> potentialPasswordText = new ArrayList<>();
		
		//Populates the password strings array
		java.io.File inFile = new java.io.File("resources/passwords.txt");
		try {
			Scanner fileReader = new Scanner(inFile);
			
			while (fileReader.hasNextLine())
				potentialPasswordText.add(fileReader.nextLine());
			
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Sets the master password
		password = potentialPasswordText.get(randomNumber(0, potentialPasswordText.size() - 1)) + randomNumber(0, 9999);
		System.out.println(name + "'s password is: " + password);
		
		//Sets the secure folder password
		secureFolderPassword = potentialPasswordText.get(randomNumber(0, potentialPasswordText.size() - 1)) + randomNumber(0, 9999);
		
		homeDir.clear();
		homeFiles.clear();
		
		currentDir = new Directory("base");
		homeDir.add(currentDir);
		
		currentDir.addDirectory("secure", secureFolderPassword);
		currentDir.addDirectory("contacts");
		
		//Sets up Secure Folder
		TextFile noticeFile = new TextFile("notice.txt");
		noticeFile.setContents("NOTICE: THIS FOLDER, AND THE CONTENTS CONTAINED, IS TO BE KEPT CONFIDENCIAL!"
				+ "\nDo to the confidencial nature of this folder, any attempt to modify, move, or delete files contained in any way will be blocked");
		
		SecureFile passwordFile = new SecureFile("password.sec", password);

		currentDir.getDirectorys().get(currentDir.getIndexOfDirectory("secure")).addFile(noticeFile);
		currentDir.getDirectorys().get(currentDir.getIndexOfDirectory("secure")).addFile(passwordFile);
		
		//Set up contacts folder
		Directory contactsFolder = currentDir.getDirectorys().get(currentDir.getIndexOfDirectory("contacts"));
		
		TextFile serverIP = new TextFile("servers.txt");
		TextFile websiteIP = new TextFile("websites.txt");
		
		websiteIP.setContents("Websites to visit\n\n"
				+ "Secure Serve - Shop for security software such as firewalls and anti-maleware software - URL: secureserve.com\n"
				+ "Breakware - Shop for malicious software and code - URL: breakware.onion\n"
				+ "Goodegg - Shop for computer parts - URL: goodegg.com\n"
				+ "Saltiens - Shop for password cracking software - URL: saltienscrackers.onion");
		
		serverIP.setContents("Important IP Addresses to Servers\n\n"
				+ "Your IP: " + playerIP + "\n"
				+ "Target's IP: " + opponentIP + "\n");
		
		contactsFolder.addFile(websiteIP);
		contactsFolder.addFile(serverIP);
		
		TextFile assignmentFile = new TextFile("assignment.txt", currentDir);
		
		assignmentFile.setContents("Hello, " + name + ". I'll skip past the warm greetings and get straight to the point.\n"
				+ "We have a potential threat to this school's security. We believe the hacker known as " + getOpponentsName() + " has tools to breach our database.\n"
						+ "They can potentially steal our cirriculumn for AP Computer Science A, and use it to promote there tech department at there private school.\n"
						+ name + ", I am assigning you to get the password to their database. We believe that it is stored on their computer. Oh, and about their computer.\n"
								+ "They may be trying to hack you at this very instance.\n"
								+ "I have been assigned by Principal A to find the hacker's password which we can use to investigate there database. I couldn't think of a better candidate to do this than you.\n"
								+ "You have 10 minutes. If the time ends, then we will call the case closed.\n"
								+ "If the attacker breaches you, your hard-drive will be wiped to protect confidencial information, and you will get a 55 for this class.\n"
								+ "If you succeed, you will get an extra 5 points and a free pokemon sticker.\n"
								+ "Good luck out there.\n"
								+ "Eric");
	
		TextFile securePasswordFile = new TextFile("securefolderpassword.txt");
		securePasswordFile.setContents("DELETE THIS FILE IMMEDIATLY AFTER VIEWING\nThe password for the secure folder is:\n" + secureFolderPassword);
		
		currentDir.addFile(assignmentFile);
		currentDir.addFile(securePasswordFile);
		
		baseDir = currentDir;
	}
	
	public void getCurrentDirectory()
	{
		if (!connectedToOpponent)
		{
			String dir = "C:\\" + getParentDirAsString(currentDir);
			out.println(dir);
		}
		else
		{
			String dir = opponentIP + "\\" + getParentDirAsString(currentDir);
			out.println(dir);
		}
	}
	
	public String getParentDirAsString(Directory dir)
	{
		if (dir.getParent() == null)
			return dir.getName();
		
		return getParentDirAsString(dir.getParent()) + "\\" + dir.getName();
	}
	
	public void clipboard(Directory dir, boolean move)
	{
		copiedDirectory = dir;
		copiedFile = null;
		
		if (move)
			currentDir.getDirectorys().remove(dir);
	}
	
	public void clipboard(File file, boolean move)
	{
		copiedFile = file;
		copiedDirectory = null;
		
		if (move)
			currentDir.getFiles().remove(file);
	}
	
	//---------------------------Util Checking Methods and Stuff-------------------------\\
	
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
	
	//Sends a message to the client
	public void sendMessageToClient(String message)
	{
		out.println(message);
	}
	
	public boolean checkForMessage(String message)
	{
		return in.nextLine().equals(message);
	}
	
	//----------------------Getters and Setters----------------------\\
	
	public Socket getSocket()
	{
		return socket;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setOpponentName(String name)
	{
		opponentName = name;
	}
	
	public String getOpponentsName()
	{
		return opponentName;
	}
	
	public String getOpponentIP()
	{
		return opponentIP;
	}
	
	public void setOpponentIP(String ip)
	{
		opponentIP = ip;
	}
	
	public String getPlayerIP()
	{
		return playerIP;
	}
	
	public void setPlayerIP(String ip)
	{
		playerIP = ip;
	}

	public Directory getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(Directory baseDir) {
		this.baseDir = baseDir;
	}
}
