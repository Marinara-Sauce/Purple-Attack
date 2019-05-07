
//Class runs the connection blocker stuff
public class ConnectionBlocker {

	private int level;
	private Processor processor;
	
	private boolean opponentConnected = false;
	private long startTime;
	
	public ConnectionBlocker(int level, Processor processor) 
	{
		this.level = level;	
		this.processor = processor;
	}
	
	public ConnectionBlocker(Processor processor)
	{
		level = 1;
		this.processor = processor;
	}
	
	public void startTimer()
	{
		startTime = System.currentTimeMillis();
		opponentConnected = true;
	}

	//Gets number of cores running this
	public int numCores()
	{
		return processor.getNumFocusedCores(2);
	}
	
	public boolean terminated()
	{
		long startTimeSeconds = startTime / 1000;
		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		long timePassed = currentTimeSeconds - startTimeSeconds;
		
		final long DIFF_NEEDED = (60 - (15 * level)) - (2 * numCores());
		
		return timePassed >= DIFF_NEEDED;
	}

	public void terminatedConnection()
	{
		opponentConnected = true;
	}
	
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

	public boolean isOpponentConnected() {
		return opponentConnected;
	}

	public void setOpponentConnected(boolean opponentConnected) {
		this.opponentConnected = opponentConnected;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}
