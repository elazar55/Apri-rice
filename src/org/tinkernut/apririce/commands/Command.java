package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public interface Command extends Runnable{
	void run();

	void execPriv(final Bot bot, final User sender, final String params, final MessageEvent me);
}