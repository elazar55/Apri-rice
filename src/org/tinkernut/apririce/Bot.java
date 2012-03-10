// TODO: Make threaded

package org.tinkernut.apririce;

import java.util.HashMap;

import org.tinkernut.apririce.commands.AnnounceCommand;
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.commands.DefineCommand;
import org.tinkernut.apririce.commands.HelpCommand;
import org.tinkernut.apririce.textUtils.Parser;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.events.IRCEvent;
import jerklib.events.JoinEvent;
import jerklib.events.MessageEvent;
import jerklib.events.QuitEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;

public class Bot implements IRCEventListener, Runnable {
	/**
	 * Globals
	 */
	private String ircServer;
	private String channelName;
	private ConnectionManager con;
	private HashMap<String, Command> commandsMap;
	private final String CMD_START = "|";
	private Command announceCommand;

	Thread t1;
	/**
	 * Class constructor
	 */
	public Bot(String server, String channel) {
		// Initialize globals		
		commandsMap = new HashMap<String, Command>();
		announceCommand = new AnnounceCommand();

		ircServer = server;
		channelName = channel;

		// TODO: Create storage
		// Bot profile (nick)
		con = new ConnectionManager(new Profile("Apri-rice"));
	}

	//Thread start
	public void run() {
		// Request Connection to server
		try {
			con.requestConnection(ircServer).addIRCEventListener(this);
		}
		catch (Exception e) {
			System.out.println("Failed to connect to channel. Exiting.");
			return;
		}
	}
	/**
	 * Event handler
	 */
	public void receiveEvent(IRCEvent e) {
		Type type = e.getType();
		// Connection to server successful
		if (type == Type.CONNECT_COMPLETE) {
			e.getSession().join(channelName);

			// Connection to channel successful
		} else if (type == Type.JOIN_COMPLETE) {
			// TODO: Register + identify bot

			// User successfuly joins channel
		} else if (type == Type.JOIN) {
			JoinEvent je = (JoinEvent) e;

			// User successfuly leaves channel
		} else if (type == Type.QUIT) {
			QuitEvent qe = (QuitEvent) e;

			// Message successfuly recieved in channel
		} else if (type == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent) e; 

			// Check and execute any commands
			if (me.getMessage().startsWith(CMD_START)) {
				String commandString = Parser.stripCommand(me.getMessage());

				Command helpCommand = new HelpCommand();
				Command defineCommand = new DefineCommand();

				commandsMap.put("help", helpCommand);
				commandsMap.put("define", defineCommand);
				commandsMap.put("announce", announceCommand);

				if (commandsMap.containsKey(commandString)) {
					// TODO: Finish threading implementation
					commandsMap.get(commandString).init(Parser.stripAguments(me.getMessage()), me);

					t1 = new Thread(commandsMap.get(commandString));
					t1.start();
				}
				// TODO: Check for private message
			}
		}
	}
}
