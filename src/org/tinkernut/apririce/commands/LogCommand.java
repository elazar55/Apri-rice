package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class LogCommand  implements Command{
	Bot bot;
	String params;
	MessageEvent me;

	public void init(String params, MessageEvent me, Bot bot) {
		this.bot = bot;
		this.params = params;
		this.me = me;
	}

	public void run() {
		if (params.startsWith("start")) {
			if (!bot.isLogging) {
				TextBuffer.addAndDisplay("Logging started", me);
				bot.isLogging = true;
			}else {
				TextBuffer.addAndDisplay("Logging is already initiated.", me);
			}
		}else if (params.startsWith("stop")) {
			if (bot.isLogging) {
				TextBuffer.addAndDisplay("Logging stopped", me);
				bot.isLogging = false;				
			}else {				
				TextBuffer.addAndDisplay("Logging is already not running.", me);
			}
		}else {
			TextBuffer.addAndDisplay("Invalid option.", me);
		}
	}

	public void execPriv(String user) {
		
	}

}
