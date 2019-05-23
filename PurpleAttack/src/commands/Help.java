package commands;
public class Help implements _Commands
{
	private static final String[] COMMANDS = {"help", "clear or clr", "dir", "cd", "mkdir", "rmdir", "rm", "mv", "cp"};
	private static final String[] DESCRIPTIONS = {"Displays help", "Clears the console", "Shows all files and directories", "Changes the selected directory", "Creates a new directory", "Removes a directory", "Removes a file", "Moves a file", "Copies a file"};
	
	public static void run() 
	{
		for (int i = 0 ; i < COMMANDS.length ; i++)
			System.out.println(COMMANDS[i] + " - " + DESCRIPTIONS[i]);
	}
}
