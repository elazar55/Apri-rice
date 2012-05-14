package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;

public class QuitCommand extends Command {
	Bot bot;

	public void init(String params, MessageEvent me, Bot bot) {
		super.init(params, me, bot);
		this.bot = bot;
	}

	public void exec() {
		bot.con.quit();
		System.exit(0);
	}

	public void execPriv() {
		exec();
	}
	
}
