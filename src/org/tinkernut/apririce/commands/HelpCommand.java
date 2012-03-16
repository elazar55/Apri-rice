package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;

public class HelpCommand implements Command {
	MessageEvent me;
	
	public void init(String params, MessageEvent me, Bot bot) {
		this.me = me;
	}
	
	public void run() {
		if (me.getChannel() == null) {
			execPriv();
		}else {
			exec();
		}
	}
	
	public void exec() {
		System.out.println("This is the public secotr!");
	}

	public void execPriv() {
		System.out.println("This is the private secotr!");
	}
}
