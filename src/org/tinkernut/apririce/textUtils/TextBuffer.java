package org.tinkernut.apririce.textUtils;

import jerklib.events.MessageEvent;

public class TextBuffer {
	//TODO: Overload display() function as to take a User, as to display private messages.

	private static String tBuffer = "";

	/**
	 * Adds string to a buffer.
	 * @param text
	 */
	public static final void add(final String text) {
		tBuffer += text;
	}
	/**
	 * Adds text to a buffer, and displays the buffer to either the public channel, or private user if in a PM.
	 * @param text
	 * @param me
	 */
	public static final void addAndDisplay(final String text, MessageEvent me) {
		tBuffer += text;
		
		if (me.getChannel() == null) {
			displayPriv(me);
		} else {
			display(me);			
		}
	}
	public static final void addArray(final String[] textArray) {
		for (int i = 0; i < textArray.length; i++) {
			tBuffer += textArray[i] + " ";
		}
	}
	public static final void display(final MessageEvent me) {
		me.getChannel().say(tBuffer);
		tBuffer = "";
	}
	
	public static final void displayPriv(final MessageEvent me) {
		me.getSession().sayPrivate(me.getNick(), tBuffer);
		tBuffer = "";
	}
}