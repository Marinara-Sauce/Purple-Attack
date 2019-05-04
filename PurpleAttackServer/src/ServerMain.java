import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

	public static void main(String[] args) throws IOException {
		System.out.println("Purple Attack: Created by Dan Bliss: Server");
		System.out.println("////-------------------------------////");
		
		Settings.setup();
		
		Game game = new Game();
		System.out.println("The Server is running!");
		
		ExecutorService pool = Executors.newFixedThreadPool(20);
		
		try (ServerSocket listener = new ServerSocket(59989))
		{
			while (true)
				pool.execute(new Handler(listener.accept(), game));
		}
	}
}
