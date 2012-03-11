package org.tinkernut.apririce.commands;

import java.util.Timer;
import java.util.TimerTask;
import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class AnnounceCommand implements Command{
	int interval = 0;
	String announcement = "";
	String params;
	MessageEvent me;
	Timer timer;

	public void init(String params, MessageEvent me, Bot bot) {
		this.params = params;
		this.me = me;
	}

	public void run() {
		//set argument
		if (params.startsWith("set")) {
			//check if required arguments are missing
			if (Parser.stripAguments(params).equalsIgnoreCase("")) {
				TextBuffer.addAndDisplay("Enter an interval or announcement to set.", me);
			}
			//set interval or announcement
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
		//start argument
		if (params.startsWith("start")) {
			start();
		}
		if (params.startsWith("stop")) {
			timer.cancel();
			TextBuffer.addAndDisplay("Announcement stopped.", me);
		}
	}
	
	public void set(int interval) {
		this.interval = interval*1000;
		TextBuffer.addAndDisplay("Interval succsessfuly set to: " + interval + " seconds", me);
	}
	
	public void set(String announcment) {
		this.announcement = announcment;
		TextBuffer.addAndDisplay("Announcement succsessfuly set to: " + this.announcement, me);
	}
	
	public void start() {
		if (interval == 0) {
			TextBuffer.addAndDisplay("Set an interval in seconds. |announce set <interval here(eg. 100, 300, 134, etc,.)>", me);
		}else if (announcement.equals("")) {
			TextBuffer.addAndDisplay("Set an announcement. |announce set <message here>", me);
		}else {			
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					TextBuffer.addAndDisplay(announcement, me);
				}
			}, 0, interval);
		}
		
	}

	public void execPriv(String user) {
		
	}
}