package websites;

public interface _Website 
{
	String title = "<WEBSITE-TITLE>";
	String url = "<WEBSITE_URL>";
	int health = 1000;
	
	public void run();
	public void delay(int millis);
}
