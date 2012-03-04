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
import org.tinkernut.apririce.textUtils.TextBuffer;

public class DefineCommand extends Command {
	Map<String, URL> urlMap;
	@Override
	public void init(final Bot b, final String s, final MessageEvent m) {
		super.init(b, s, m);
		urlMap = new HashMap<String, URL>();

		try {
			urlMap.put("urban", new URL("http://www.urbandictionary.com/define.php?term="));
		}catch (MalformedURLException e) {
			new TextBuffer().addAndDisplay("Internal error: Malformed URL", me);
		}
	}

	public void run() {

	}

	protected void execPriv(final Bot bot, final User sender, final String params, final MessageEvent me) {
		
	}
}
