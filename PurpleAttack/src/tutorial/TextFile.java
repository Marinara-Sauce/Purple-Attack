package tutorial;

import java.io.PrintWriter;

public class TextFile extends File
{
	private String contents;
	
	public TextFile(String name) {
		super(name);
	}
	
	public TextFile(String name, Directory directory) {
		super(name, directory);
	}

	public void editContents()
	{
		System.out.println("Enter some text into this text file. Press enter to save");
	}
	
	public void readContents(PrintWriter out)
	{
		System.out.println("Printing: " + name);
		out.println("Reading: " + name);
		out.println("");
		out.println(contents);
		out.println("");
		out.println("-END OF FILE-");
	}
	
	//---------------------GETTERS AND SETTERS--------------------------\\
	
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
