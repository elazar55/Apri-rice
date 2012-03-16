package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;

public class QuitCommand implements Command {
	
	MessageEvent me;
	Bot bot;

	public void init(String params, MessageEvent me, Bot bot) {
		this.me = me;
		this.bot = bot;
	}

	public void run() {
		if (me.getChannel() == null) {
			exec();
		} else {
			execPriv();
		}
	}

	public void exec() {
		bot.con.quit();
	}

	public void execPriv() {
		exec();
	}
	
}
