package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public class HelpCommand implements Command {
	MessageEvent me;
	
	public HelpCommand(MessageEvent m) {
		me = m;
	}
	
	public void run() {
		
	}
	
	public void execPriv(Bot bot, User sender, String params, MessageEvent me) {

	}
}
