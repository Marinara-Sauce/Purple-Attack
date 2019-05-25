import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//Used in the Inbox, contains a message contents, recepiant, and timestamp
public class Message {

	private final DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	private String sender;
	private String content;
	private Date timeSent;
	
	public Message(String sender, String content, Date timeSent) 
	{
		this.sender = sender;
		this.content = content;
		this.timeSent = timeSent;
	}
	
	public void printMessage()
	{
		System.out.println("[" + format.format(timeSent) + "] " + sender + " - " + content);
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(Date timeSent) {
		this.timeSent = timeSent;
	}

	public DateFormat getFormat() {
		return format;
	}

}
