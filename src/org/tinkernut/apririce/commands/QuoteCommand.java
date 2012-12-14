package org.tinkernut.apririce.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.tinkernut.apririce.textUtils.TextBuffer;

public class QuoteCommand extends Command{

	@Override
	void exec() {
		try {
			// Connect to quotes website.
			URLConnection urlConnection = new URL("http://www.quotationspage.com/random.php3").openConnection();
			// Get input stream of HTML Source and put to a BufferedReader.
			BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			// Put HTML Source stream to single line string until quote is reached.
			String HTMLSource = "";
			
			while (!HTMLSource.contains("</a> </dt><dd class=\"author\">")) {
				HTMLSource += bReader.readLine();
			}
			
			// Start quote index.
			int startQuoteIndex = HTMLSource.indexOf(".html\">", HTMLSource.indexOf("href=\"/quote/")) + 7;
			// End quote index.
			int endQuoteIndex = HTMLSource.indexOf("</a>", startQuoteIndex);
			
			String definition = HTMLSource.substring(startQuoteIndex, endQuoteIndex);
			
			// Start author index.
			int startAuthorIndex = HTMLSource.indexOf("/\">", HTMLSource.indexOf("<b><a href=\"/quotes/")) + 3;
			// End author index.
			int EndAuthorIndex = HTMLSource.indexOf("</a>", startAuthorIndex);
			
			String author = HTMLSource.substring(startAuthorIndex, EndAuthorIndex);
			
			// Display definition, and author.
			TextBuffer.addAndDisplay(definition + " - " + author, me);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			TextBuffer.addAndDisplay("Unable to connect to quotes database.", me);
		} catch (IOException e) {
			e.printStackTrace();
			TextBuffer.addAndDisplay("Unable to connect to quotes database.", me);
		}
	}

	@Override
	void execPriv() {
		
	}

}
