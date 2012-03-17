package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;

public class UserCommand implements Command{
	private MessageEvent me;
	private String params;

	public void init(String params, MessageEvent me, Bot bot) {
		this.params = params;
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
		if (params.startsWith("warn")) {
			
		}
	}

	public void execPriv() {
		
	}

}
