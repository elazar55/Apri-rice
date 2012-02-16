package org.tinkernut.apririce;

import java.util.HashMap;
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.commands.HelpCommand;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.events.IRCEvent;
import jerklib.events.JoinEvent;
import jerklib.events.MessageEvent;
import jerklib.events.QuitEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;

public class Bot implements IRCEventListener {
	/**
	 * Globals
	 */
	private ConnectionManager con;
	private Command helpCommand;
	private HashMap<String, Command> commands;
	private final String CMD_START = "|";
	/**
	 * Class constructor
	 */
	public Bot() {
		// Initialize globals
		helpCommand = new HelpCommand();
		
		commands = new HashMap<String, Command>();
		commands.put("help", helpCommand);

		// TODO: Create storage
		// Bot profile (nick)
		con = new ConnectionManager(new Profile("Apri-rice"));
		// Request Connection to server
		con.requestConnection("irc.geekshed.net").addIRCEventListener(this);
	}
	
	/**
	 * Event handler
	 */
	public void receiveEvent(IRCEvent e) {
		Type type = e.getType();
		// Connection to server successful
		if (type == Type.CONNECT_COMPLETE) {
			e.getSession().join("#tinkernut_test_room");
			
		// Connection to channel successful
		} else if (type == Type.JOIN_COMPLETE) {
			// TODO: Register + identify bot
			
		// User successfuly joins channel
		} else if (type == Type.JOIN) {
			JoinEvent je = (JoinEvent) e;
			
		// User successfuly leaves channel
		} else if (type == Type.QUIT) {
			QuitEvent qe = (QuitEvent) e;
			
		// Messaged successfuly recieved in channel
		} else if (type == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent) e;
			
			
		}
	}
	
}
