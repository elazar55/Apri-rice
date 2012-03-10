//TODO: Thread this!

package org.tinkernut.apririce.commands;

import java.util.Timer;
import java.util.TimerTask;
import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class AnnounceCommand implements Command{
	long interval;
	String announcement;
	String params;
	MessageEvent me;
	Timer timer;

	public void init(String params, MessageEvent me) {
		this.params = params;
		this.me = me;
	}

	public void run() {
		if (Parser.getFirstArgument(params).equalsIgnoreCase("set")) {
			if (Parser.stripAguments(params).equalsIgnoreCase("")) {
				TextBuffer.addAndDisplay("Enter an interval or announcement to set.", me);
			}
			else {
				int number;
				String words;
				try {
					number = Integer.parseInt(Parser.stripAguments(params));
					set(number);
				} catch (NumberFormatException e) {
					words = Parser.stripAguments(params);
					set(words);
				}
			}
		}
		if (Parser.getFirstArgument(params).equalsIgnoreCase("start")) {
			TextBuffer.addAndDisplay(this.announcement, me);
		}
	}
	
	public void set(int interval) {
		this.interval = interval*1000;
		TextBuffer.addAndDisplay("Interval succsessfuly set to " + interval + " seconds", me);
	}
	
	public void set(String announcment) {
		this.announcement = announcment;
		TextBuffer.addAndDisplay("Announcement succsessfuly set to " + this.announcement, me);
	}
	
	public void start() {
		timer = new Timer();
		timer.schedule(new task(me, announcement), interval);
	}

	public void execPriv(Bot bot, User sender, String params, MessageEvent me) {
		
	}


}

class task extends TimerTask{
	
	MessageEvent me;
	String announcement;
	
	public task(MessageEvent me, String announcement) {
		this.me = me;
		this.announcement = announcement;
	}

	public void run() {
		TextBuffer.addAndDisplay(announcement, me);
	}
}