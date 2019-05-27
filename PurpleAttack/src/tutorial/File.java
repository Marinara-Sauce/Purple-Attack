package tutorial;

public class File 
{

	protected String name; //Files name
	protected Directory directory; //Directory where stored, null if stored in Home directory
	
	public File(String name, Directory directory)
	{
		this.name = name;
		this.directory = directory;
	}
	
	public File(String name)
	{
		this.name = name;
		directory = null;
	}

	//---------------------GETTERS AND SETTERS--------------------------\\
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Directory getDirectory() {
		return directory;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}
	
}
