package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

public interface Command extends Runnable{
	void init(String params, MessageEvent me);
	
	void run();

	void execPriv(String user);
}