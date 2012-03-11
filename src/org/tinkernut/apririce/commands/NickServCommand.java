package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.textUtils.Parser;

import jerklib.Channel;
import jerklib.events.MessageEvent;

public class NickServCommand implements Command {
	String params;
	MessageEvent me;
	Bot bot;

	public void init(String params, MessageEvent me, Bot bot) {
		this.params = params;
		this.me = me;
		this.bot = bot;
	}

	public void run() {
	}

	public void execPriv(String user) {
		if (params.startsWith("identify")) {
			me.getSession().sayPrivate("nickserv", "identify " + Parser.stripAguments(params));
		}else if (params.startsWith("register")) {
			me.getSession().sayPrivate("nickserv", "register " + Parser.stripAguments(params));
		}else if (params.startsWith("group")) {
			me.getSession().sayPrivate("nickserv", "group " + Parser.stripAguments(params));
		}else {
			System.out.println("Invalid arguments.");
			me.getSession().sayPrivate(me.getNick(), "Invalid arguments.");
			me.getSession().sayChannel(new Channel(bot.channelName, me.getSession()), "Invalid arguments.");
		}
	}
}
