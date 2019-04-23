package game;

public class SecureFile extends File {
	
	private String contents;
	
	public SecureFile(String name, Directory directory) {
		super(name, directory);
	}

	public SecureFile(String name) {
		super(name);
	}
	
	public SecureFile(String name, String contents)
	{
		super(name);
		this.contents = contents;
	}
	
	public void decrypt()
	{
		//TODO: Decryption Process
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
