package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public class HelpCommand implements Command {
	Bot bot;
	String params;
	MessageEvent me;
	
	public void init(Bot b, String s, MessageEvent m) {
		bot = b;
		params = s;
		me = m;
	}
	
	public void run() {
		
	}
	
	public void execPriv(Bot bot, User sender, String params, MessageEvent me) {

	}
}
