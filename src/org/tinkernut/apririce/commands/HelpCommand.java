package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class HelpCommand implements Command {
	MessageEvent me;
	
	public void init(String params, MessageEvent me, Bot bot) {
		this.me = me;
	}
	
	public void run() {
		//TODO: something
		TextBuffer.addAndDisplay("I'm sorry Dave, I'm afraid I can't let you do that.", me);
	}
	
	public void execPriv(String user) {

	}
}
