import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Decryptor 
{
	private int level;
	private Processor processor;
	
	private long startTime;
	private boolean timerRunning;
	
	private boolean decryptingFolder;
	private String decryptingFileName;
	
	public Decryptor(int level, Processor processor) 
	{
		this.level = level;
		this.processor = processor;
	}
	
	public Decryptor(Processor processor)
	{
		this.processor = processor;
		level = 1;
	}

	public void startTimer(boolean folder, String fileName)
	{
		startTime = System.currentTimeMillis();
		timerRunning = true;
		decryptingFolder = folder;
		decryptingFileName = fileName;
		
		if (folder)
			System.out.println("Starting Decryption on folder: " + fileName + "(Allocate More Cores to Speed the Process)");
		else
			System.out.println("Starting Decryption on file: " + fileName + "(Allocate More Cores to Speed the Process)");
	}
	
	public boolean checkTimer()
	{
		if (!timerRunning)
			return false;
		
		int coresRunning = processor.getNumFocusedCores(4);
		long startTimeInSeconds = startTime / 1000;
		long neededTime = startTimeInSeconds + (60 - (15 * level) - (2 * coresRunning));
		long currentTimeInSeconds = System.currentTimeMillis() / 1000;
		
		if (currentTimeInSeconds >= neededTime)
		{
			timerRunning = false;
			
			if (decryptingFolder)
				System.out.println("Finished Decrypting: " + decryptingFileName + ". The folder has been unlocked");
			else
				System.out.println("Finished Decrypting: " + decryptingFileName + ". The file has been saved as: " + decryptingFileName + ".txt");
			
			return true;
		}
		
		return false;
	}
	
	public void runTick(PrintWriter out)
	{
		if (checkTimer())
		{	
			if (decryptingFolder)
			{
				System.out.println("The folder has been placed in your current directory");
				out.println("FINISHEDDECRYPTFOLDER" + decryptingFileName);
			}
			else
			{
				System.out.println("The file has been saved as out.txt");
				out.println("FINISHEDDECRYPT" + decryptingFileName);
			}
		}
	}
	
	//Takes in the socket and the print writer from the client for communication between the server and client
	public void processCommand(String command, PrintWriter out, Scanner in)
	{
		String c = command.replace("decrypt ", "");
		
		if (c.equalsIgnoreCase("help"))
			System.out.println("Decryptor is a program that decrypts passwords to .sec files, or folders.\nCurrent Version: V" + level + "\nCOMMANDS:\nhelp - Shows this\ncrack - Starts the decryption process");
		
		else if (c.startsWith("crack"))
		{
			String fileName;
			//Starts cracking process
			if (c.length() == "crack".length())
			{
				Scanner input = new Scanner(System.in);
				System.out.println("Enter the file or folder to decrypt (File must be of type .sec)");
				fileName = input.nextLine();
			}
			else
				fileName = c.replace("crack ", "");
			
			out.println("DECRYPT" + fileName);
			
			while (in.hasNextLine())
			{
				String line = in.nextLine();
				
				if (line.startsWith("CONFIRMED"))
				{
					line = line.replace("CONFIRMED", "");
					
					if (line.contains("FOLDER"))
						startTimer(true, command.replace("decrypt crack ", ""));
					else
						startTimer(false, command.replace("decrypt crack ", ""));
					
					break;
				}
				else if (line.startsWith("NOFILE"))
				{
					System.out.println("Could not find file or folder: " + fileName);
					break;
				}
			}
		}
		
		else System.out.println("Unknown Command! Type decrypt help for a list of commands");
	}
	
	//----------------------------------GETTERS AND SETTERS-------------------------------//
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public boolean isTimerRunning() {
		return timerRunning;
	}

	public void setTimerRunning(boolean timerRunning) {
		this.timerRunning = timerRunning;
	}
}
