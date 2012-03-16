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
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.commands.website.characterReplacement;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class DefineCommand implements Command {
	String params;
	MessageEvent me;
	URLConnection urlConnection;

	public void init(String params, MessageEvent me, Bot bot) {
		this.params = params;
		this.me = me;
	}
	
	public void run() {
		if (me.getChannel() == null) {
			execPriv();
		}else {
			exec();
		}
	}

	public void exec() {		
		Map<String, website> urlMap = new HashMap<String, website>();
		try {
			urlMap.put("urban", new website("<div class=\"definition\">", characterReplacement.PERCENT, new URL("http://www.urbandictionary.com/define.php?term=")));
		} catch (MalformedURLException e1) {
			new TextBuffer();
			TextBuffer.addAndDisplay("Malformed URL.", me);
		}

		if (params.contains(" ")) {			
			try {
				
				if (urlMap.containsKey(Parser.getFirstArgument(params))) {
					//Replace special characters in to be defined String
					String urlAddon = Parser.stripArguments(params);
					if (urlMap.get(Parser.getFirstArgument(params)).charReplacement.equals(characterReplacement.PERCENT)) {
						urlAddon = urlAddon.replace(" ", "%20");
					}
					
					//Append definition String to url
					urlMap.get(Parser.getFirstArgument(params)).url = new URL(urlMap.get(Parser.getFirstArgument(params)).url.toString() + urlAddon); 

					//Establish connection and download HTML source
					urlConnection = urlMap.get(Parser.getFirstArgument(params)).url.openConnection();
					BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					TextBuffer.addAndDisplay("Connection successful.", me);

					//Put whole page source into a single line string
					String HTMLSource = "";

					while((bReader.readLine()) != null) {
						HTMLSource += bReader.readLine();
					}
					HTMLSource = HTMLSource.replace("<br/>", " ");
					HTMLSource = HTMLSource.replace("&quot;", "\"");
					HTMLSource = HTMLSource.replace("\n\t", " ");
					HTMLSource = HTMLSource.replace("  ", " ");

					//Extract definition
					int start = HTMLSource.indexOf(urlMap.get(Parser.getFirstArgument(params)).startingTag) + urlMap.get(Parser.getFirstArgument(params)).startingTag.length();
					int end = HTMLSource.indexOf("<", start);

					String definition = HTMLSource.substring(start, end);
					new TextBuffer();
					TextBuffer.addAndDisplay(definition, me);
				}else {
					new TextBuffer();
					TextBuffer.addAndDisplay("Invalid site.", me);
				}
			} catch (IOException e) {
				new TextBuffer();
				TextBuffer.addAndDisplay("Unable to establish connection.", me);
			}
		}else if(!urlMap.containsKey(Parser.getFirstArgument(params))){			
			new TextBuffer();
			TextBuffer.addAndDisplay("Invalid site.", me);
		}else {			
			new TextBuffer();
			TextBuffer.addAndDisplay("Input something to define.", me);
		}
	}

	public void execPriv() {

	}
}

class website{
	String startingTag;
	characterReplacement charReplacement;

	URL url;
	public website(String startingTag, characterReplacement charReplacement, URL url) {
		this.startingTag = startingTag;
		this.charReplacement = charReplacement;
		this.url = url;
	}

	public enum characterReplacement{
		PERCENT, UNDERSCORE;
	}
}
