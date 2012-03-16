// TODO: Make threaded

package org.tinkernut.apririce;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.tinkernut.apririce.commands.AnnounceCommand;
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.commands.DefineCommand;
import org.tinkernut.apririce.commands.HelpCommand;
import org.tinkernut.apririce.commands.LogCommand;
import org.tinkernut.apririce.commands.NickServCommand;
import org.tinkernut.apririce.commands.QuitCommand;
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
	public String channelName;
	public boolean isLogging = false;
	private final String CMD_START = "|";
	private String ircServer;
	public ConnectionManager con;
	private HashMap<String, Command> commandsMap;
	private BufferedWriter bLogWriter;
	private String botName;
	//Global instance commands
	private Command announceCommand;

	Thread publicT1, privateT1;
	/**
	 * Class constructor
	 */
	public Bot(String server, String channel, String botName) {
		// Initialize globals		
		commandsMap = new HashMap<String, Command>();
		
		announceCommand = new AnnounceCommand();

		ircServer = server;
		channelName = channel;
		this.botName = botName;

		// TODO: Create storage
		// Bot profile (nick)
		con = new ConnectionManager(new Profile(botName));
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
		System.out.println(e.getRawEventData());

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
			if (isLogging) {
				try {
					bLogWriter = new BufferedWriter(new FileWriter("log.txt", true));
					bLogWriter.write(e.getRawEventData());
					bLogWriter.newLine();
					bLogWriter.close();
				} catch (IOException e1) {
					System.out.println("Error. Could not open log file.");
				}
			}
			
			MessageEvent me = (MessageEvent) e;
			// Check and execute any commands
			if (me.getMessage().startsWith(CMD_START)) {
				String commandString = Parser.stripCommand(me.getMessage());

				//Local instance commands
				Command helpCommand = new HelpCommand();
				Command defineCommand = new DefineCommand();
				Command logCommand = new LogCommand();
				Command quitCommand = new QuitCommand();

				//Put identifier and associated command
				commandsMap.put("help", helpCommand);
				commandsMap.put("define", defineCommand);
				commandsMap.put("announce", announceCommand);
				commandsMap.put("log", logCommand);
				commandsMap.put("quit", quitCommand);

				if (commandsMap.containsKey(commandString)) {
					// TODO: Finish threading implementation
					commandsMap.get(commandString).init(Parser.stripArguments(me.getMessage()), me, this);

					publicT1 = new Thread(commandsMap.get(commandString));
					publicT1.start();
				}else {
					me.getChannel().say("Not a command.");
				}
			}
			// Private message successfuly recieved
		} else if (type == Type.PRIVATE_MESSAGE) {
			MessageEvent me = (MessageEvent) e;
			if (me.getMessage().startsWith(CMD_START)) {			
				// Check and execute any commands
				String commandString = Parser.stripCommand(me.getMessage());

				//Local instance commands
				Command helpCommand = new HelpCommand();
				Command nickServCommand = new NickServCommand();
				Command logCommand = new LogCommand();
				Command quitCommand = new QuitCommand();

				//Put identifier and associated command
				commandsMap.put("nickserv", nickServCommand);
				commandsMap.put("log", logCommand);
				commandsMap.put("help", helpCommand);
				commandsMap.put("quit", quitCommand);

				if (commandsMap.containsKey(commandString)) {
					// TODO: Finish threading implementation
					commandsMap.get(commandString).init(Parser.stripArguments(me.getMessage()), me, this);
					commandsMap.get(commandString).run();
				}
				else {
					me.getSession().sayPrivate(me.getNick(), "Not a command.");
				}
			}
		}
	}
}
