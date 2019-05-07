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
			//The music I am using was not added to the repo due to A. File size and B. Copyright.
			//You can use your own code, or use the music I used (First 20 Minutes). Either way, name the file music.wav and put it in the res folder
			//Music used: https://youtu.be/UNlopG6VFkM
			System.err.println("Could not find file: " + fileName);
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
