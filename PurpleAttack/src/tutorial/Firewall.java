package tutorial;

public class Firewall 
{
	private int level;
	private int numCores;
	
	public Firewall(int level, int numCores)
	{
		this.level = level;
		this.numCores = numCores;
	}

	public Firewall()
	{
		level = 1;
		numCores = 0;
	}
	
	public void processCommand(String command)
	{
		command = command.replace("FIREWALL", "");
		
		if (command.contains("CORES"))
		{
			numCores = Integer.parseInt(command.replace("CORES", ""));
		}
		
		else if (command.contains("LEVEL"))
		{
			level = Integer.parseInt(command.replace("LEVEL", ""));
		}
	}
	
	//Returns true if the connection is blocked, returns false otherwise
	public boolean blockConnection()
	{	
		int random = (int) (Math.random() * 100);
		int needToBlock = (10 * level) + (2 * numCores);
		System.out.println(random + " / " + needToBlock);
		return random <= needToBlock;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNumCores() {
		return numCores;
	}

	public void setNumCores(int numCores) {
		this.numCores = numCores;
	}
	
}
