import java.text.DecimalFormat;

public class BitcoinMiner 
{
	private final int MAX_CORES = 6;
	private final String HELP_TEXT = "Bitcoin is an program to manage and mine for bitcoins. Each command listed below has to be prefixed with bitcoin\nCOMMANDS\nhelp - displays this\nview - displays info";
	private double amount; //Number of bitcoins in wallet
	private int cores; //Number of cores the user is using
	private int version; //Can influence how fast the mine mines
	
	private Game game; //Current Game
	private Processor processor;
	
	private double mineRate; //Number of bitcoins per second based on mine rate
	
	//For calculating number of bitcoins
	private long mineStartTime;
	
	public BitcoinMiner(Game game, Processor processor)
	{
		this.game = game;
		this.processor = processor;
		
		amount = 0;
		cores = 1;
		version = 1;
		
		updateMineRate();
	}

	//Called from game, processes a given command
	public void processCommand(String command)
	{
		if (command.equalsIgnoreCase("help"))
			System.out.println(HELP_TEXT);
		
		else if (command.equalsIgnoreCase("view"))
		{
			DecimalFormat df = new DecimalFormat("##.##");
			System.out.println("Bitcoin Miner and Wallet (Version " + version + ")");
			System.out.println("-----------------------------------------------------");
			System.out.println("Current Balance: " + amount + " BTC");
			System.out.println("Current Balance in USD: $" + df.format(btcToUSD()));
			System.out.println("Current Number of Cores: " + cores);
			System.out.println("Current Mining Rate: " + mineRate + " BTC/s");
			System.out.println("");
		}
		
		else
			System.out.println("Unknown command! Type bitcoin help for more commands");
	}
	
	//Gets the number of cores focused on bitcoin mining
	public int getNumOfCores()
	{
		return processor.getNumFocusedCores(1);
	}
	
	//Calculates how many bitcoins the miner mined
	public void updateBitcoinAmount()
	{
		long currentTime = System.currentTimeMillis();
		
		long timePassedMillis = currentTime - mineStartTime;
		double secondsPassed = (double)(timePassedMillis * (1000));
		
		amount += mineRate * secondsPassed;
		
		mineStartTime = System.currentTimeMillis();
	}
	
	public void updateMineRate()
	{
		//Each core that is mining allows for a increase of 0.0025 bitcoins per second.
		//Each version adds another 0.001 bitcoin per second
		cores = getNumOfCores();
		mineRate = (0.000000000025 * (double)(cores)) /* + (0.000000000000001 * (double)(version)) */;
	}
	
	//Purchases an item. Returns true if it was successful, returns false if it wasn't
	public boolean purchase(double price)
	{
		if (price <= amount)
		{
			buyItem(price);
			return true;
		}
		
		return false;
	}
	
	//Attempts to buy an item. Called from purchase.
	private void buyItem(double price)
	{
		amount -= price;
		System.out.println("Spent " + price + " BTC. New Balance: " + amount);
	}
	
	//Returns the value in USD
	public double btcToUSD()
	{
		return amount * 5000;
	}
	
	public void startMining()
	{
		mineStartTime = System.currentTimeMillis();
	}
	
	//-------------------------GETTERS AND SETTERS--------------------------//
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		
		if (cores <= MAX_CORES && cores > 0)
			this.cores = cores;
		else
			System.err.println("You cannot set the core count to " + cores);
		
		updateMineRate();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public double getMineRate() {
		return mineRate;
	}
}
