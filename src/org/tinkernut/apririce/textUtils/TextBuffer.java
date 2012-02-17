package org.tinkernut.apririce.textUtils;

import jerklib.events.MessageEvent;

public class TextBuffer {

	private String tBuffer = "";

	public final void add(final String text) {
		tBuffer += text;
	}
	public final void addArray(final String[] textArray) {
		for (int i = 0; i < textArray.length; i++) {
			tBuffer += textArray[i];
		}
	}
	public final void display(final MessageEvent me) {
		me.getChannel().say(tBuffer.concat(" "));
	}
	public void wrapText(final String text) {
	}
}