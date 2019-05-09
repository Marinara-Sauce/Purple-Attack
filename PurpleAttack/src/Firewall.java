//Initialized in the inventory, controls the firewall
public class Firewall {

	private Game game;
	
	private int level;
	private Processor processor;
	
	public Firewall(int level, Processor processor, Game game) 
	{
		this.level = 0;
		this.processor = processor;
		this.game = game;
	}
	
	public Firewall(Processor processor, Game game)
	{
		level = 0;
		this.processor = processor;
		this.game = game;
	}
	
	//Returns number of cores focusing on this
	public int getNumCores()
	{
		return processor.getNumFocusedCores(3);
	}
	
	//Returns true if the connection is blocked, returns false otherwise
	public boolean blockConnection()
	{	
		return true;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		game.getPrintWriter().println("FIREWALLLEVEL" + level);
	}
	
	public Game getGame() {
		return game;
	}

}
