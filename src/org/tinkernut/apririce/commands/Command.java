package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.Bot;

import jerklib.events.MessageEvent;

public interface Command extends Runnable{
	void init(String params, MessageEvent me, Bot bot);
	
	void run();

	void execPriv();
}