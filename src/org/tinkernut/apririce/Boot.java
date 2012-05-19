package org.tinkernut.apririce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Boot {
	public static void main(String[] args) {
		//Create thread(s) for bot.
		//		Thread tinkernutTestThread = new Thread(new Bot("irc.geekshed.net", "#tinkernut_test_room", "Apri-rice_tinkernut-test-room"));
		//		
		//		tinkernutTestThread.start();


		File channelFile = new File("channels.txt");
		int numberOfChannels = 0;

		try {
			// Create channels file if it doesn't exist
			if (!channelFile.exists()) {
				channelFile.createNewFile();
			}

			BufferedReader bReader = new BufferedReader(new FileReader(channelFile));

			// Count number of channels as to make enough threads
			while (bReader.readLine() != null) {
				numberOfChannels++;
			}
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
		}else {
			// Loop to make threads, whilst inputing appropriate stuff into constructor. Gawd I suck at comments
			//TODO: pass unique info
			for (int i = 0; i < numberOfChannels; i++) {
				Thread thread = new Thread(new Bot("irc.geekshed.net", "#tinkernut_test_room", "Apri-rice_tinkernut-test-room"));
				thread.start();
			}			
		}
	}
}
