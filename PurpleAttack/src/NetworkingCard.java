//All this class really has is a variable that represents the card's level, and the health (Health goes down on a DDOS attack)
public class NetworkingCard {

	private int health; //The averege time it takes to connect to servers
	private int update;
	private int ddosCount;
	
	public NetworkingCard() 
	{
		update = 1;
		health = 1500;
		
		ddosCount = 0;
	}
	
	public NetworkingCard(int update, int health)
	{
		this.health = health;
		this.update = update;
		
		ddosCount = 0;
	}

	private void calculateHealthVar()
	{
		health = 1500 + (250 * update) + (1000 * ddosCount);
	}
	
	public void update()
	{
		update++;
		calculateHealthVar();
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
		calculateHealthVar();
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
		calculateHealthVar();
	}
}
