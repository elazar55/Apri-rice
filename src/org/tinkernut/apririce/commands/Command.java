package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;
import jerklib.events.MessageEvent;

public abstract class Command implements Runnable{
	protected String params;
	protected MessageEvent me;
	String helpText = "Insert help text here.";
	
	public void init(String params, MessageEvent me, Bot bot) {
		this.params = params;
		this.me = me;
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
	
	abstract void exec();

	abstract void execPriv();
}