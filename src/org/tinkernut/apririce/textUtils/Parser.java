package org.tinkernut.apririce.textUtils;

public class Parser {
	public static String stripCommand(String cmd) {
		if (cmd.contains(" ")) {
			return cmd.substring(1, cmd.indexOf(" "));
		}
		else {
			return cmd.substring(1);
		}
	}
	public static String stripArguments(String args) {
		if (args.contains(" ")) {
			return args.substring(args.indexOf(" ")+1);
		}
		else {
			return "";
		}
	}
	public static String getFirstArgument(String args) {
		if (args.contains(" ")) {			
			return args.substring(0, args.indexOf(' '));
		}else {
			return args;
		}
	}
}