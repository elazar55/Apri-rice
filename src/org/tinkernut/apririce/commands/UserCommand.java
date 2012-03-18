package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;
import org.tinkernut.apririce.textUtils.Parser;

public class UserCommand extends Command{
	Bot bot;
	
	@Override
	public void initPriv(String params, MessageEvent me, Bot bot, User user) {
		super.initPriv(params, me, bot, user);
		this.bot = bot;
	}
	
	public void exec() {
	}

	public void execPriv() {
		if (params.startsWith("warn")) {
			try {
				if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params)))).warnings == 3) {
					me.getChannel().kick(bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params)))).getNick(), "3 strikes! You're OUT!");
					return;
				}				
			} catch (IndexOutOfBoundsException e) {
				me.getSession().sayPrivate(me.getNick(), "User does not exist.");
				return;
			}
			bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params)))).warnings++;
		}else {
			me.getSession().sayPrivate(me.getNick(), "Invalid arguments.");
		}
	}
}
