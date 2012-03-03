// TODO: Make threaded

package org.tinkernut.apririce;

import java.util.HashMap;
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.commands.DefineCommand;
import org.tinkernut.apririce.commands.HelpCommand;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
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

	public TextBuffer textBuffer;
	private HashMap<String, Command> commandsMap;
	private final String CMD_START = "|";
	
	Thread t1, t2;
	/**
	 * Class constructor
	 */
	public Bot() {
		// Initialize globals
		commandsMap = new HashMap<String, Command>();
		
		commandsMap.put("help", new HelpCommand());
		commandsMap.put("define", new DefineCommand());
		
		textBuffer = new TextBuffer();

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

			// Message successfuly recieved in channel
		} else if (type == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent) e;

			// Check and execute any commands
			if (me.getMessage().startsWith(CMD_START)) {
				String commandString = Parser.stripCommand(me.getMessage());
				if (commandsMap.containsKey(commandString)) {
					
					Command command = (commandsMap.get(commandString));
					command.init(this, Parser.stripArguments(me.getMessage()), me);
					
					command.run();
					
					// TODO: Finish threading implementation
					t1 = new Thread(command);
					t2 = new Thread(command);
				}
				// TODO: Check for private message
			}
		}
	}
}
