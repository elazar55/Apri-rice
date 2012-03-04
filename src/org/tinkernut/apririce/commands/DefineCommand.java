// TODO: Finish DefineCommand

package org.tinkernut.apririce.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;
import org.tinkernut.apririce.commands.Command;

public class DefineCommand extends Command {
	Map<String, URL> urlMap;
	@Override
	public void init(final Bot b, String s, MessageEvent m) {
		super.init(b, s, m);
		urlMap = new HashMap<String, URL>();
		
		try {
		urlMap.put("urban", new URL("http://www.urbandictionary.com/define.php?term="));
		}catch (MalformedURLException e) {
			
		}
	}
	
	public void run() {
		
	}

	protected void execPriv(final Bot bot, User nder, String params, MessageEvent me) {
		
	}
}
