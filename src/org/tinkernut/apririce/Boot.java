package org.tinkernut.apririce;

public class Boot {
	public static void main(String[] args) {
		Thread tinkernutThread = new Thread(new Bot("irc.geekshed.net", "#tinkernut_test_room"));
		
		tinkernutThread.start();
	}
}
