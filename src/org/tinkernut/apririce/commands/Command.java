package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public interface Command extends Runnable{
	void init(Bot b, String s, MessageEvent m);

	void execPriv(final Bot bot, User nder, String params, MessageEvent me);
}
