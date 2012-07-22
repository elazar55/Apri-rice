package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.Bot;
import jerklib.events.MessageEvent;

public abstract class Command implements Runnable{
	//TODO: Pass User to constructor, hence finish user tracking!
	protected String params;
	protected MessageEvent me;
	protected Bot bot;
	final protected String helpText = "Some help text.";
	
	public void init(String params, MessageEvent me, Bot bot) {
		this.params = params;
		this.me = me;
		this.bot = bot;
	}
	
	public void initPriv(String params, MessageEvent me, Bot bot) {
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
	
	public String getHelpText() {
		return helpText;
	}
	
	abstract void exec();

	abstract void execPriv();
}