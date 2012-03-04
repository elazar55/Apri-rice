package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public abstract class Command implements Runnable{
	private Bot bot;
	private String params;
	private MessageEvent me;
	
	public void init(final Bot b, String s, MessageEvent m) {
		bot = b;
		params = s;
		me = m;
	}

	public abstract void run();

	protected abstract void execPriv(final Bot bot, User nder, String params, MessageEvent me);
}