package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;
import org.tinkernut.apririce.textUtils.Parser;

public class NickServCommand implements Command {
	String params;
	MessageEvent me;

	public void init(String params, MessageEvent me) {
		this.params = params;
		this.me = me;
	}

	public void run() {
	}

	public void execPriv(Bot bot, User sender, String params, MessageEvent me) {
		if (params.startsWith("register")) {
			me.getSession().sayPrivate("nickserv", "register" + Parser.stripAguments(params));
		}		
	}
}
