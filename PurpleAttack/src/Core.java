public class Core 
{

	public int focus; //Chooses the focus of the core, wether it be firewall, bitcoin, etc. 0 - Nothing, 1 - Bitcoin, 2 - Firewall
	
	public Core()
	{
		focus = 0;
	}
	
	public Core(int focus)
	{
		this.focus = focus;
	}
	
	public String getFocusAsString()
	{
		final String focuses[] = {"Inactive", "Bitcoin Mining", "Connection Blocker", "Firewall", "Decryptor"};
		
		return focuses[focus];
	}
	
	public int getFocus()
	{
		return focus;
	}
	
	public void setFocus(int focus)
	{
		this.focus = focus;
	}
	
}
