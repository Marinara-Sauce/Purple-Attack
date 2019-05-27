package tutorial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Class not in use

public class Tutorial {

	private Directory currentDir;
	
	private String password;
	private String opponentPassword;
	
	private List<Directory> homeDir = new ArrayList<>();
	private List<File> homeFiles = new ArrayList<>();
	private Directory baseDir;
	
	private String secureFolderPassword;
	
	private String playerIP;
	private String opponentIP;
	
	private boolean connectedToOpponent = false;
	
	public void runTutorial()
	{
		clearConsole();
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to Purple Attack!");
		System.out.println("This is a game where you attempt to hack and steal the other player's password.");
		System.out.println("The game simulates a DOS terminal, and is command driven. The game is also complicated.");
		System.out.println("This tutorial may contain some terminology that may make it confusing for someone who dosne't use computers, such as Directory. If you are confused, there is a quick reference guide in the manual");
		System.out.println("When you are ready, hit enter");
		input.nextLine();
		
		clearConsole();
		setupFileSystem();
		
		System.out.println("The game simulates a computer, and therefore will have files and folders. Imagine that this window is it's own computer. It has a hard drive, processor, and software running in the background.\n"
				+ "The computer has no idea what to do, so it's going to need someone to enter commands. Below this text you will see what is called a prompt. It shows the current directory you're in, and waits for input.\n"
				+ "Well start with a simple command. Type \"help\" into the line.");
	}

	public void setupFileSystem()
	{	
		//Sets the master password
		password = "Fireflies2019";
		
		//Sets the secure folder password
		secureFolderPassword = "ComputerScience9200";
		
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
				+ "BitBlocker - Shop for security software such as Firewalls and Blockers - URL: www.bitblock.com\n"
				+ "Goodegg - Shop for computer parts - URL: www.goodegg.com\n"
				+ "Crackersploit - Shop for software for cracking .sec files and folders - URL: crackersploit.onion");
		
		serverIP.setContents("Important IP Addresses to Servers\n\n"
				+ "Your IP: " + playerIP + "\n"
				+ "Target's IP: " + opponentIP + "\n");
		
		serverIP.setContents("We are fetching the IP address of your opponent. It will appear here in approxomatly 2 minutes\n"
				+ "Your IP: " + playerIP);
		
		contactsFolder.addFile(websiteIP);
		contactsFolder.addFile(serverIP);
		
		TextFile assignmentFile = new TextFile("assignment.txt", currentDir);
		
		assignmentFile.setContents("This file has some generic info related to the story, and will be more interesting in the game. Generating the file in the game takes some server side functions to work though, so I can't add it in here, but you can still finish the tutorial.");
	
		TextFile securePasswordFile = new TextFile("securefolderpassword.txt");
		securePasswordFile.setContents("DELETE THIS FILE IMMEDIATLY AFTER VIEWING\nThe password for the secure folder is:\n" + secureFolderPassword);
		
		currentDir.addFile(assignmentFile);
		currentDir.addFile(securePasswordFile);
		
		baseDir = currentDir;
		
		opponentPassword = "VerySecurePassword123";
	}
	
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
	
	public void delay(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getInput(Scanner input)
	{	
		if (!connectedToOpponent)
		{
			String dir = "C:\\" + getParentDirAsString(currentDir);
			System.out.println(dir);
		}
		else
		{
			String dir = opponentIP + "\\" + getParentDirAsString(currentDir);
			System.out.println(dir);
		}
		
		System.out.println(" > ");
		return input.nextLine();
	}
	
	public String getParentDirAsString(Directory dir)
	{
		if (dir.getParent() == null)
			return dir.getName();
		
		return getParentDirAsString(dir.getParent()) + "\\" + dir.getName();
	}
	
	public void showHelp()
	{
		final String[] COMMANDS = {"help", "clear or clr", "dir", "cd", "mkdir", "rmdir", "rm", "mv", "cp"};
		final String[] DESCRIPTIONS = {"Displays help", "Clears the console", "Shows all files and directories", "Changes the selected directory", "Creates a new directory", "Removes a directory", "Removes a file", "Moves a file", "Copies a file"};
		
		for (int i = 0 ; i < COMMANDS.length ; i++)
			System.out.println(COMMANDS[i] + " - " + DESCRIPTIONS[i]);
	}
}
