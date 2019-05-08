//Initialized in the inventory, controls the firewall
public class Firewall {

	private int level;
	private Processor processor;
	
	public Firewall(int level, Processor processor) 
	{
		this.level = 0;
		this.processor = processor;
	}
	
	public Firewall(Processor processor)
	{
		level = 0;
		this.processor = processor;
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
	}
	
	

}
