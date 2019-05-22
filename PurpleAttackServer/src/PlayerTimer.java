
public class PlayerTimer implements Runnable{

	private Player player;
	private boolean ran = false;
	
	public PlayerTimer(Player player)
	{
		this.player = player;
	}
	
	@Override
	public void run() 
	{
		if (!ran)
		{
			player.unlockIPAddress();
			ran = true;
		}
	}

}
