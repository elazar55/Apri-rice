package org.tinkernut.apririce;

public class Boot {
	public static void main(String[] args) {
		//Create thread(s) for bot.
		Thread tinkernutTestThread = new Thread(new Bot("irc.geekshed.net", "#tinkernut_test_room", "Apri-rice_tinkernut-test-room"));
		Thread tinkernut = new Thread(new Bot("irc.geekshed.net", "#tinkernut", "Apri-rice_tinkernut"));
		
		tinkernutTestThread.start();
		tinkernut.start();
	}
}
