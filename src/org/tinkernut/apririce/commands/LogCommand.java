package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class LogCommand  extends Command{
	Bot bot;

	@Override
	public void init(String params, MessageEvent me, Bot bot) {
		super.init(params, me, bot);
		this.bot = bot;
	}

	public void exec() {
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

	public void execPriv() {
		if (params.startsWith("start")) {
			if (!bot.isLogging) {
				me.getSession().sayPrivate(me.getNick(), "Logging started.");
				bot.isLogging = true;
			}else {
				me.getSession().sayPrivate(me.getNick(), "Logging is already initiated.");
			}
		}else if (params.startsWith("stop")) {
			if (bot.isLogging) {
				me.getSession().sayPrivate(me.getNick(), "Logging stopped.");
				bot.isLogging = false;				
			}else {				
				me.getSession().sayPrivate(me.getNick(), "Logging is already not running.");
			}
		}else {
			me.getSession().sayPrivate(me.getNick(), "Invalid option.");
		}
	}
}
