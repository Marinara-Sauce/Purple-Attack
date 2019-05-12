import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Processor 
{
	private Game game;
	
	private final String HELP_TEXT = "processor help - Displays this\nprocessor info - Views the current processor's information\nprocessor view-cores - View each cores current status\nprocessor edit-cores - Edits each cores current status";

	private String name; //Processors Name
	
	private BitcoinMiner miner; //Gets set from within game
	
	private int numCores;
	
	private List<Core> cores;
	
	public Processor(int numCores)
	{
		this.numCores = numCores;
		cores = new ArrayList<>();
		
		initCores();
	}
	
	//Used within inventory, used when adding item
	public Processor(String name, int numCores, Game game)
	{
		this.numCores = numCores;
		this.name = name;
		
		cores= new ArrayList<>();
		this.game = game;
		initCores();
		setBitcoinMiner(game.getBitcoinMiner());
	}
	
	public Processor(Game game)
	{
		numCores = 4;
		cores = new ArrayList<>();
		
		this.game = game;
		initCores();
	}
	
	public void setBitcoinMiner(BitcoinMiner miner)
	{
		this.miner = miner;
	}

	public void processCommand(String command)
	{
		if (command.equalsIgnoreCase("help"))
			System.out.println(HELP_TEXT);
		else if (command.equalsIgnoreCase("view-cores"))
			displayCores();
		else if (command.equalsIgnoreCase("edit-cores"))
			editCores();
		else
			System.out.println("Unknown Command! Type processor help for help");
	}
	
	public void initCores()
	{
		for (int i = 0 ; i < numCores ; i++)
			cores.add(new Core());
	}
	
	public int getNumFocusedCores(int focus)
	{
		int numFocused = 0;
		
		for (int i = 0 ; i < cores.size() ; i++)
		{
			if (cores.get(i).getFocus() == focus)
				numFocused++;
		}
		
		return numFocused;
	}
	
	//-----------------------------COMMANDS---------------------------------------------//
	
	public void displayCores()
	{
		System.out.println("Number of Cores: " + numCores);
		
		//Gets number of active and inactive cores
		int activeCores = 0;
		int inActiveCores = 0;
		
		for (int i = 0 ; i < cores.size() ; i++)
		{
			if (cores.get(i).getFocus() == 0)
				inActiveCores++;
			else
				activeCores++;
		}
		
		System.out.println("Active: " + activeCores + " | Inactive: " + inActiveCores);
		
		//Displays Each cores status
		
		for (int i = 0 ; i < cores.size() ; i++)
		{
			System.out.println("Core " + (i + 1) + ": " + cores.get(i).getFocusAsString());
		}
	}
	
	public void editCores()
	{
		while (true)
		{
			displayCores();
			System.out.print("Enter a core to modify (Enter -1 to exit): ");
			try
			{
				Scanner input = new Scanner(System.in);
				int selection = input.nextInt();
				
				if (selection == -1)
					break;
				
				if (selection == 0 || selection > numCores)
					System.out.println("That core does not exsist!");
				else
				{
					Core core = cores.get(selection - 1);
					System.out.println("Please enter a focus (0 - Inactive, 1 - Bitcoin Mining, 2 - Connection Blocker, 3 - Firewall");
					core.setFocus(input.nextInt());
					System.out.println("Success!");
				}
				
				miner.updateMineRate();
				game.getPrintWriter().println("FIREWALLCORES" + getNumFocusedCores(3));
			}
			catch (InputMismatchException e)
			{
				System.out.println("You must input a number!");
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Invalid Option!");
			}
		}
	}
	
	//-----------------------------GETTERS AND SETTERS----------------------------------//
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getNumCores() {
		return numCores;
	}

	public void setNumCores(int numCores) {
		this.numCores = numCores;
	}

	public List<Core> getCores() {
		return cores;
	}

	public void setCores(List<Core> cores) {
		this.cores = cores;
	}
	
	
	
}
