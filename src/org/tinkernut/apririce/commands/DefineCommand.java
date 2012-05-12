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
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.commands.website.characterReplacement;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class DefineCommand extends Command {
	URLConnection urlConnection;

	public void exec() {		
		Map<String, website> urlMap = new HashMap<String, website>();
		try {
			urlMap.put("urban", new website("<div class=\"definition\">", "</div><div class=\"example\">", characterReplacement.PERCENT, new URL("http://www.urbandictionary.com/define.php?term=")));
			urlMap.put("dic", new website("<div class=\"dndata\">", "</div></div><div class=\"luna-Ent\"><span class=\"dnindex\">", characterReplacement.UNDERSCORE, new URL("http://dictionary.reference.com/browse/")));
		} catch (MalformedURLException e1) {
			new TextBuffer();
			TextBuffer.addAndDisplay("Malformed URL.", me);
		}

		if (params.contains(" ")) {			
			try {

				if (urlMap.containsKey(Parser.getFirstArgument(params.toLowerCase()))) {
					//Replace special characters in to be defined String
					String urlAddon = Parser.stripArguments(params);
					if (urlMap.get(Parser.getFirstArgument(params)).charReplacement.equals(characterReplacement.PERCENT)) {
						urlAddon = urlAddon.replace(" ", "%20");
					}if (urlMap.get(Parser.getFirstArgument(params)).charReplacement.equals(characterReplacement.UNDERSCORE)) {
						urlAddon = urlAddon.replace(" ", "+");
					}

					//Append definition String to url
					urlMap.get(Parser.getFirstArgument(params)).url = new URL(urlMap.get(Parser.getFirstArgument(params)).url.toString() + urlAddon); 

					//Establish connection and download HTML source
					urlConnection = urlMap.get(Parser.getFirstArgument(params)).url.openConnection();
					BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					TextBuffer.addAndDisplay("Connection successful.", me);

					//Put whole page source into a single line string
					String HTMLSource = "";

					while (!HTMLSource.contains("</html>")) {
						HTMLSource += bReader.readLine();
					}

					// Remove everything from HTMLSource except definition with unique HTML tags.
					String unusedStart = "";
					try {
						unusedStart = HTMLSource.substring(0, HTMLSource.indexOf(urlMap.get(Parser.getFirstArgument(params)).startingTag));						
					} catch (StringIndexOutOfBoundsException e) {
						TextBuffer.addAndDisplay("Definition doesn't exist, or error.", me);
						return;
					}
					
					String unusedEnd = HTMLSource.substring(HTMLSource.indexOf(urlMap.get(Parser.getFirstArgument(params)).endingTag));
					
					HTMLSource = HTMLSource.replace(unusedStart, "");
					HTMLSource = HTMLSource.replace(unusedEnd, "");
					HTMLSource = HTMLSource.replace("&quot;", "\"");
					HTMLSource = HTMLSource.replace("\n\t", " ");
					HTMLSource = HTMLSource.replace("  ", " ");

					String tagReplace = "";
					while (!tagReplace.equals(null)) {
						try {
							int startTag = HTMLSource.indexOf("<");							
							int endTag = HTMLSource.indexOf(">") + 1;
							tagReplace = HTMLSource.substring(startTag, endTag);
							HTMLSource = HTMLSource.replace(tagReplace, "");						
						} catch (IndexOutOfBoundsException e) {
							break;
						}
					}

					//Extract definition
					//					int start = 0;
					//					int end = HTMLSource.indexOf(urlMap.get(Parser.getFirstArgument(params)).endingTag, start);

					String definition = HTMLSource;
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
	String endingTag;
	characterReplacement charReplacement;

	URL url;

	public website(String startingTag, String endingTag, characterReplacement charReplacement, URL url) {
		this.startingTag = startingTag;
		this.endingTag = endingTag;
		this.charReplacement = charReplacement;
		this.url = url;
	}

	public enum characterReplacement{
		PERCENT, UNDERSCORE;
	}
}
