// TODO: Finish DefineCommand
package org.tinkernut.apririce.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class DefineCommand extends Command {
	Map<String, String> urlMap;
	URLConnection urlConnection;
	InputStreamReader inputStream;
	BufferedReader bReader;

	@Override
	public void init(final Bot b, final String s, final MessageEvent m) {
		super.init(b, s, m);
		urlMap = new HashMap<String, String>();
		urlMap.put("urban", "http://www.urbandictionary.com/define.php?term=");
	}

	public void run() {
		if (params.contains("urban") && Parser.stripAguments(params) != "") {
			try {
				urlConnection = new URL(urlMap.get(params.substring(0, params.indexOf(' ')))+Parser.stripAguments(params)).openConnection();
				bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			} catch (IOException e) {
				new TextBuffer().addAndDisplay("Unable to establish connection.", me);
			}
		}
		else {
			new TextBuffer().addAndDisplay("Input something to define", me);
		}
	}

	protected void execPriv(final Bot bot, final User sender, final String params, final MessageEvent me) {

	}
}
