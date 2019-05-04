package game;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//A class that contains lists and functionalitys for directories and files.
public class Directory 
{
	//Contains all the files and directorys in the folder
	private List<File> files;
	private List<Directory> directorys;
	
	private String name; //Name of directory
	private String password; //Password to access directory
	private Directory parent; //A reference to the parent directory, equals null if in base
	private boolean protect;
	
	public Directory(String name)
	{
		this.name = name;
		parent = null;
		password = null;
		protect = false;
		
		files = new ArrayList<>();
		directorys = new ArrayList<>();
	}
	
	public Directory (String name, Directory parent)
	{
		this.name = name;
		this.parent = parent;
		password = null;
		protect = false;
		
		files = new ArrayList<>();
		directorys = new ArrayList<>();
	}
	
	public Directory (String name, String password)
	{
		this.name = name;
		this.password = password;
		protect = true;
		parent = null;
		
		files = new ArrayList<>();
		directorys = new ArrayList<>();
	}

	public Directory (String name, String password, Directory parent)
	{
		this.name = name;
		this.password = password;
		this.parent = parent;
		protect = true;
		files = new ArrayList<>();
		directorys = new ArrayList<>();
	}
	
	public void addDirectory(String name)
	{
		directorys.add(new Directory(name, this));
	}
	
	public void addDirectory(String name, String password)
	{
		directorys.add(new Directory(name, password, this));
	}
	
	public void addDirectory(Directory dir)
	{
		directorys.add(dir);
	}
	
	public void addFile(File file)
	{
		files.add(file);
	}
	
	public void displayDirectory(PrintWriter output)
	{
		output.println("Found " + files.size() + " files and " + directorys.size() + " directories in directory " + name);
		
		for (int i = 0 ; i < directorys.size() ; i++)
			output.println("Dir: " + directorys.get(i).getName());
		
		for (int i = 0 ; i < files.size() ; i++)
			output.println("Files: " + files.get(i).getName());
		
		output.println("ENDOFLINE");
	}
	
	public int getIndexOfDirectory(String name)
	{
		int dirIndex = -1;
		
		for (int i = 0 ; i < directorys.size() ; i++)
		{
			if (directorys.get(i).getName().equals(name))
			{
				dirIndex = i;
				break;
			}
		}
		
		return dirIndex;
	}
	
	public int getIndexOfFile(String name)
	{
		int dirIndex = -1;
		
		for (int i = 0 ; i < files.size() ; i++)
		{
			if (files.get(i).getName().equals(name))
			{
				dirIndex = i;
				break;
			}
		}
		
		return dirIndex;
	}
	
	//---------------------GETTERS AND SETTERS--------------------------\\
	
	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public List<Directory> getDirectorys() {
		return directorys;
	}

	public void setDirectorys(List<Directory> directorys) {
		this.directorys = directorys;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Directory getParent() {
		return parent;
	}

	public void setParent(Directory parent) {
		this.parent = parent;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isProtect() {
		return protect;
	}

	public void setProtect(boolean protect) {
		this.protect = protect;
	}
}
