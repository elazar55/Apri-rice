package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.textUtils.Parser;
import jerklib.Channel;

public class NickServCommand extends Command {
	
	public void exec() {
		
	}

	public void execPriv() {
		if (params.startsWith("identify")) {
			me.getSession().sayPrivate("nickserv", "identify " + Parser.stripArguments(params));
		}else if (params.startsWith("register")) {
			me.getSession().sayPrivate("nickserv", "register " + Parser.stripArguments(params));
		}else if (params.startsWith("group")) {
			me.getSession().sayPrivate("nickserv", "group " + Parser.stripArguments(params));
		}else {
			System.out.println("Invalid arguments.");
			me.getSession().sayPrivate(me.getNick(), "Invalid arguments.");
			me.getSession().sayChannel(new Channel(bot.channelName, me.getSession()), "Invalid arguments.");
		}
	}

}
