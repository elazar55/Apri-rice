package org.tinkernut.apririce;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Boot {
	public static void main(String[] args) {
		//Create thread(s) for bot.
		//		Thread tinkernutTestThread = new Thread(new Bot("irc.geekshed.net", "#tinkernut_test_room", "Apri-rice_tinkernut-test-room"));
		//		
		//		tinkernutTestThread.start();


		File channelFile = new File("channels.txt");
		BufferedReader bReader;
		BufferedWriter bWriter;
		String fileBuffer = "";
		int numberOfChannels = 0;
		LinkedList<String> serverList = new LinkedList<String>();
		LinkedList<String> channelList = new LinkedList<String>();
		final String commentSymbol = "||";

		try {
			// Create channels file if it doesn't exist
			if (!channelFile.exists()) {
				channelFile.createNewFile();
				bWriter = new BufferedWriter(new FileWriter(channelFile));
				bWriter.write("|| Lines starting with || are ignored. Format this like so : irc.server.com/#someChannel\r\n" + 
						"|| A config file for the channel will be generated, in which you can then edit the nick and password.");
				bWriter.close();
			}

			bReader = new BufferedReader(new FileReader(channelFile));

			// Count number of channels as to make enough threads and put the server and respectivechannel in
			// respective lists whilst ignoring lines starting with a commentSymbol

			while ((fileBuffer = bReader.readLine()) != null) {
				if (!fileBuffer.startsWith(commentSymbol)) {					
					numberOfChannels++;

					String server = fileBuffer.substring(0, fileBuffer.indexOf("/"));
					String channel = fileBuffer.substring(fileBuffer.indexOf("/") + 1);

					serverList.push(server);
					channelList.push(channel);
				}
			}
			fileBuffer = "";
			bReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Internal I/O error. File " + channelFile.getName() + "doesn't exist... it should.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File " + channelFile.getName() +  " inaccessible.");
			e.printStackTrace();
		}
		System.out.println(numberOfChannels);

		// If user didn't edit the channels config, quit
		if (numberOfChannels < 1) {
			System.out.println("Put the server and channel info in " + channelFile.getName());
			System.exit(0);
		}else {
			// Loop to make threads, whilst inputing appropriate stuff into constructor. Gawd I suck at comments

			try {
				bReader = new BufferedReader(new FileReader(channelFile));

				Iterator<String> serverListIterator = serverList.iterator();
				Iterator<String> channelListIterator = channelList.iterator();

				for (int i = 0; i < numberOfChannels; i++) {
					String server = serverListIterator.next();
					String channel = channelListIterator.next();
					String nick = "Apri-rice_" + channel.replace("#", "");

					System.out.println(serverList.getLast());
					System.out.println(channelList.getLast());

					Thread thread = new Thread(new Bot(server, channel, nick));
					thread.start();
				}

				bReader.close();
			} catch (FileNotFoundException e) {
				System.out.println("Internal I/O error. File " + channelFile.getName() + "doesn't exist... it should.");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("File " + channelFile.getName() +  " inaccessible.");
				e.printStackTrace();
			}
		}
		while (true) {
			int lastThreadCount = Thread.activeCount();

			for (int i = 0; i < 8; i++) {				
				if (Thread.activeCount() == i && lastThreadCount != i) {
					System.out.println("Total thread count changed to "+ i);
				}

			}
		}
	}
}
