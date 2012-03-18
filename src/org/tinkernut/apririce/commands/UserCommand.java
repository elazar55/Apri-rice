package org.tinkernut.apririce.commands;

import jerklib.Channel;
import jerklib.events.MessageEvent;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.Rank;
import org.tinkernut.apririce.User;
import org.tinkernut.apririce.textUtils.Parser;

public class UserCommand extends Command{
	Bot bot;

	@Override
	public void initPriv(String params, MessageEvent me, Bot bot, User user) {
		super.initPriv(params, me, bot, user);
		this.bot = bot;
	}
	@Override
	public void init(String params, MessageEvent me, Bot bot){
		super.init(params, me, bot);
		this.bot = bot;
	}

	public void exec() {
		if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))) != null) {
			if (params.toLowerCase().startsWith("status")) {
				me.getChannel().say(Parser.stripArguments(params) + " has " + bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).warnings + " warnings and " + bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank + " privileges.");
			}else if (params.toLowerCase().startsWith("promote")) {
				if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank == Rank.Admin) {
					me.getChannel().say("User already has Admin privileges.");
				}else {
					bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank = Rank.Admin;
					me.getChannel().say(Parser.stripArguments(params).trim() + " is now Admin");					
				}
			}else if (params.toLowerCase().startsWith("demote")) {
				if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank == Rank.Standard) {
					me.getChannel().say(Parser.stripArguments(params) + " already has Standard privileges.");
				}else {
					bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank = Rank.Standard;
				}
			}else if (params.toLowerCase().startsWith("warn")) {
				if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).warnings == 3) {
					me.getChannel().kick(bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).getNick(), "3 strikes! You're OUT!");
					return;
				}else {
					bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).warnings++;
				}
			}
		}else {
			me.getChannel().say("User does not exist");				
		}
	}

	public void execPriv() {
		if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))) != null) {
			if (params.toLowerCase().startsWith("warn")) {
				if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).warnings == 3) {
					new Channel(bot.channelName, me.getSession()).kick(bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).getNick(), "3 strikes! You're OUT!");
					return;
				}else {
					bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).warnings++;
				}
			}
			else if (params.toLowerCase().startsWith("promote")) {
				if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank == Rank.Admin) {
					me.getSession().sayPrivate(me.getNick(), "User already has Admin privileges.");
				}else {
					bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank = Rank.Admin;					
				}
			}
			else if (params.toLowerCase().startsWith("demote")) {
				if (bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank == Rank.Standard) {
					me.getSession().sayPrivate(me.getNick(), "User already has Standard privileges.");
				}
				else {
					bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank = Rank.Standard;
				}
			}
			else if (params.toLowerCase().startsWith("status")) {
				me.getSession().sayPrivate(me.getNick(), Parser.stripArguments(params) + " has " + bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).warnings + " warnings and " + bot.userList.get(bot.userList.indexOf(new User(Parser.stripArguments(params.toLowerCase().trim())))).rank + " privileges.");
			}
		}
		else {
			me.getSession().sayPrivate(me.getNick(), "User does not exist.");
		}
	}
}
