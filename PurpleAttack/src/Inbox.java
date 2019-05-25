import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Inbox {

	List<Message> messages;
	
	public Inbox() 
	{
		messages = new ArrayList<>();
	}
	
	public void processCommand(String command, PrintWriter out, Game game)
	{
		command = command.replace("inbox ", "");
		if (command.contains("help"))
			System.out.println("Inbox is a program that manages messages. All commands are prefixed with \"inbox\"\nhelp - Displays this\nview - Views messagess\nsend - Starts the message sending dialog");
		else if (command.contains("view"))
			viewMessages();
		else if (command.contains("send"))
			sendMessage(out, game);
		else
			System.out.println("Unknown Command! Type inbox help for a list of commands");
	}
	
	public void viewMessages()
	{
		System.out.println(messages.size() + " messages in inbox");
		
		if (messages.size() == 0)
			return;
		
		for (int i = 0 ; i < messages.size() ; i++)
		{
			System.out.print((i + 1) + ". ");
			messages.get(i).printMessage();
		}
		
		Scanner input = new Scanner(System.in);
		
		while (true)
		{
			System.out.println("Select a message to delete (Type -1 to exit)");
			int selection = input.nextInt();
			
			if (selection == -1)
				return;
			
			try
			{
				messages.remove(selection - 1);
				
				if (messages.size() == 0)
					return;
			}
			catch (IndexOutOfBoundsException e)
			{
				System.out.println("Invalid Index!");
			}
		}
	}
	
	public void sendMessage(PrintWriter out, Game game)
	{
		Scanner input = new Scanner(System.in);
		String content = "";
		while (true)
		{
			System.out.println("Sending message to target. Type the conent of the message");
			content = input.nextLine();
			
			if (content.contains("`"))
				System.out.println("Illegal Character: `");
			
			break;
		}
		
		System.out.print("Sending...");
		
		out.println("SENDMESSAGE" + game.getName() + "`" +content);
		
		System.out.println("Sent!");
		
	}
	
	public void addMessage(Message message)
	{
		messages.add(message);
		System.out.println("You have 1 new message! (Type \"inbox view\" to view it)");
	}
	
	public void addMessage(Message[] message)
	{
		for (Message m : message)
			messages.add(m);
		
		System.out.println("You have "+ message.length +" new messages! (Type \"inbox view\" to view them)");
	}
	
	public void addMessage(String sender, String content, Date date)
	{
		messages.add(new Message(sender, content, date));
		System.out.println("You have 1 new message! (Type \"inbox view\" to view it)");
	}
}
