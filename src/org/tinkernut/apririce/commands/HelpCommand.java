package org.tinkernut.apririce.commands;

public class HelpCommand extends Command {
	public void exec() {
		System.out.println("This is the public secotr!");
	}

	public void execPriv() {
		System.out.println("This is the private secotr!");
		System.out.println("sup" + user.nick);
	}
}
