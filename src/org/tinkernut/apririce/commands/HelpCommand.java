package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.textUtils.TextBuffer;

public class HelpCommand extends Command {
	public void exec() {
		TextBuffer.addAndDisplay("I'm sorry Dave. I'm afraid I can't let you do that.", me);
	}

	public void execPriv() {
		System.out.println("This is the private secotr!");
		System.out.println("sup" + user.getNick());
	}
}
