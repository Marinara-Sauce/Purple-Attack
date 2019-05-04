import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//This is a class with static methods that are used to retrieve and store settings as variables
public class Settings {

	private static boolean enableAudio;
	
	//Reads the settings from the config.properties file
	public static void setup()
	{
		Properties props = new Properties();
		InputStream input = null;
		
		try 
		{
			input = new FileInputStream("settings.properties");
			props.load(input);
			
			//Settings Go Here
			enableAudio = propertyToBool(props.getProperty("enableAudio"));
			
			System.out.println("Finished Loading Settings File!");
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Takes in either a 0 or 1, converts it to true or false
	public static boolean propertyToBool(String prop)
	{
		int in = Integer.parseInt(prop);
		return in == 1;
	}

	//--------------------------GETTERS AND SETTERS--------------------------//
	public static boolean isEnableAudio() {
		return enableAudio;
	}
}
