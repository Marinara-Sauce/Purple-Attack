import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//Stores all the items the user may access when playing the game
public class Inventory {

	private Game game;
	
	private List<Processor> processors;
	private List<NetworkingCard> networkingCards;
	
	private Processor equippedProcessor;
	private NetworkingCard equippedNetworkingCard;
	
	private ConnectionBlocker connectionBlocker;
	private int blockerLevel;
	
	private Firewall firewall;
	
	public Inventory(Game game) 
	{
		this.game = game;
		
		processors = new ArrayList<>();
		processors.add(new Processor("Intel i5 6500", 4, game));
		equippedProcessor = processors.get(0);
		
		networkingCards = new ArrayList<>();
		networkingCards.add(new NetworkingCard());
		equippedNetworkingCard = networkingCards.get(0);
		
		connectionBlocker = new ConnectionBlocker(1, equippedProcessor);
		firewall = new Firewall(1, equippedProcessor, game);
		blockerLevel = 0;
	}
	
	public void addToInventory(Processor processor)
	{
		processors.add(processor);
		System.out.println("Added " + processor.getName() + " to Inventory!");
	}
	
	public void addToInventory(NetworkingCard networkingCard)
	{
		networkingCards.add(networkingCard);
		System.out.println("Added " + networkingCard.getName() + " to Inventory!");
	}
	
	public void processCommand(String command)
	{
		if (command.length() == "inventory".length())
		{
			showHelp();
			return;
		}
		
		String inCommand = command.replace("inventory ", "");
		
		if (inCommand.equals("help"))
			showHelp();
		else if (inCommand.equals("view"))
			viewInventory();
		else if (inCommand.equalsIgnoreCase("equip"))
			equipItem();
		else
			System.out.println("Unknown Command! Type inventory help for a list of commands!");
	}
	
	public void upgradeConnectionBlocker(int version)
	{
		connectionBlocker = new ConnectionBlocker(version, equippedProcessor);
	}
	
	public void upgradeFirewall(int version)
	{
		firewall = new Firewall(version, equippedProcessor, game);
	}
	
	public void upgradeBlocker(int version)
	{
		blockerLevel = version;
	}
	
	//---------------------------COMMANDS--------------------------------//
	
	public void showHelp()
	{
		System.out.println("This program manages your computer hardware that you have purchased.");
		System.out.println("inventory help - Displays help");
		System.out.println("inventory view  - Shows items in inventory");
		System.out.println("inventory equip - Starts the equipping process");
	}

	public void viewInventory()
	{
		System.out.println("Your current active processor is - " + equippedProcessor.getName() + " - " + equippedProcessor.getNumCores() + " Cores");
		System.out.println("Processors in inventory:");
		
		for (int i = 0 ; i < processors.size() ; i++)
		{
			System.out.print(processors.get(i).getName());
			if (processors.get(i) == equippedProcessor)
				System.out.print(" - ACTIVE");
			System.out.println("");
		}
		
		System.out.println("");
		
		System.out.println("Your current active networking card is - " + equippedNetworkingCard.getName() + " - Efficiency Level: " + equippedNetworkingCard.getUpdate());
		System.out.println("Networking Cards in inventory:");
		
		for (int i = 0 ; i < networkingCards.size() ; i++)
		{
			System.out.print(networkingCards.get(i).getName());
			if (networkingCards.get(i) == equippedNetworkingCard)
				System.out.print(" - ACTIVE");
			System.out.println("");
		}
		
		System.out.println("Connection Blocker: Running Version " + connectionBlocker.getLevel());
		System.out.println("Firewall: Running Version " + firewall.getLevel());
	}
	
	public void equipItem()
	{
		boolean run = true;
		
		while (run)
		{
			switch (getInput("What would you like to modify?\n1. Processor\n2. Networking Card\n3. Exit", 1, 3))
			{
			//Modify Processor
			case 1:
				runProcessorEquip();
				break;
			//Same as processor, but for networking cards
			case 2:
				runNetworkingEquip();
				break;
			case 3:
				run = false;
				break;
			}
		}
	}
	
	public void runProcessorEquip()
	{
		System.out.println("Your current active processor is - " + equippedProcessor.getName() + " - " + equippedProcessor.getNumCores() + " Cores");
		//Used for verification
		List<Processor> validCPUS = new ArrayList<>();
		
		for (int i = 0 ; i < processors.size() ; i++)
		{
			if (!(processors.get(i) == equippedProcessor))
				validCPUS.add(processors.get(i));
		}
		
		if (validCPUS.size() == 0)
		{
			System.out.println("You do not have any processors!");
			return;
		}
		
		System.out.println("Avaliable Processors:");
		
		for (int i = 0 ; i < validCPUS.size() ; i++)
			System.out.println((i + 1) + ". " + validCPUS.get(i).getName() + " - " + validCPUS.get(i).getNumCores() + " Cores");
		
		boolean selecting = true;
		while (selecting)
		{
			int selection = getInput("Which processor would you like to equip? (Type 0 to cancel)", 0, validCPUS.size());
			
			if (selection == 0)
				selecting = false;
			else
			{
				equippedProcessor = validCPUS.get(selection - 1);
				connectionBlocker.setProcessor(equippedProcessor);
				System.out.println("Euqipped " + validCPUS.get(selection - 1).getName());
			}
		}
	}
	
	public void runNetworkingEquip()
	{
		System.out.println("Your current active networking card is - " + equippedNetworkingCard.getName() + " - Efficiency Level: " + equippedNetworkingCard.getUpdate());
		//Used for verification
		List<NetworkingCard> validCPUS = new ArrayList<>();
		
		for (int i = 0 ; i < networkingCards.size() ; i++)
		{
			if (!(networkingCards.get(i) == equippedNetworkingCard))
				validCPUS.add(networkingCards.get(i));
		}
		
		if (validCPUS.size() == 0)
		{
			System.out.println("You do not have any networkingCards!");
			return;
		}
		
		System.out.println("Avaliable networkingCards:");
		
		for (int i = 0 ; i < validCPUS.size() ; i++)
			System.out.println((i + 1) + ". " + validCPUS.get(i).getName() + " - Update " + validCPUS.get(i).getUpdate());
		
		boolean selecting = true;
		while (selecting)
		{
			int selection = getInput("Which processor would you like to equip? (Type 0 to cancel)", 0, validCPUS.size());
			
			if (selection == 0)
				selecting = false;
			else
			{
				equippedNetworkingCard = validCPUS.get(selection - 1);
				System.out.println("Euqipped " + validCPUS.get(selection - 1).getName());
			}
		}
	}
	
	public static int getInput(String question, int min, int max)
	{
		Scanner in = new Scanner(System.in);
		int input;
		
		while (true)
		{
			System.out.println(question);
			try
			{
				input = in.nextInt();
				
				if (input >= min && input <= max)
					break;
				
				System.err.println("Invalid Option!");
			}
			catch (InputMismatchException e)
			{
				System.err.println("Please enter an number!");
			}
		}
		return input;
	}
	
	//-------------------------------------GETTERS AND SETTERS-------------------------------------//
	
	public List<Processor> getProcessors() {
		return processors;
	}

	public void setProcessors(List<Processor> processors) {
		this.processors = processors;
	}

	public List<NetworkingCard> getNetworkingCards() {
		return networkingCards;
	}

	public void setNetworkingCards(List<NetworkingCard> networkingCards) {
		this.networkingCards = networkingCards;
	}

	public Processor getEquippedProcessor() {
		return equippedProcessor;
	}

	public void setEquippedProcessor(Processor equippedProcessor) {
		this.equippedProcessor = equippedProcessor;
	}

	public NetworkingCard getEquippedNetworkingCard() {
		return equippedNetworkingCard;
	}

	public void setEquippedNetworkingCard(NetworkingCard equippedNetworkingCard) {
		this.equippedNetworkingCard = equippedNetworkingCard;
	}

	public int getBlockerLevel() {
		return blockerLevel;
	}

	public void setBlockerLevel(int blockerLevel) {
		this.blockerLevel = blockerLevel;
	}

	public ConnectionBlocker getConnectionBlocker() {
		return connectionBlocker;
	}

	public void setConnectionBlocker(ConnectionBlocker connectionBlocker) {
		this.connectionBlocker = connectionBlocker;
	}

	public Firewall getFirewall() {
		return firewall;
	}

	public void setFirewall(Firewall firewall) {
		this.firewall = firewall;
	}
}
