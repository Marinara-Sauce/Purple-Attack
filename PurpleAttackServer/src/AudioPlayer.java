import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	Clip clip;
	
	//Plays Audio
	public void playAudio(String fileName)
	{
		if (!Settings.isEnableAudio())
			return;
		
		File inSound = new File(fileName);
		AudioInputStream audioIn;
		try 
		{
			audioIn = AudioSystem.getAudioInputStream(inSound);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} 
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
		{
			e.printStackTrace();
		}	
	}
	
	//Pauses the playing audio
	public void stopAudio()
	{
		if (!clip.isOpen())
			return;
					
		clip.stop();
		clip.close();
	}

}
