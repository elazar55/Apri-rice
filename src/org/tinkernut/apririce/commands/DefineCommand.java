// TODO: Finish DefineCommand

package org.tinkernut.apririce.commands;

import java.io.BufferedReader;
import java.net.URL;
import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public class DefineCommand implements Command{
	
	String[] validSites = {"urban"};
	String[] siteLinks = {"http://www.urbandictionary.com/define.php?term="};
	String[] startTag  = {"<div class=\"definition\">"};
	
	URL url;
	BufferedReader bReader;
	
	int siteIndex;
	
	public void exec(Bot bot, String params, MessageEvent me) {
		if (params.equals("")) {
			bot.textBuffer.add("Choose a reference site from which to define : ");
			bot.textBuffer.addArray(validSites);
			bot.textBuffer.display(me);
		}
	}
	public void execPriv(Bot bot, User sender, String params, MessageEvent me) {
		
	}
}
