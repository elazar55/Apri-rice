package org.tinkernut.apririce;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.tinkernut.apririce.commands.AnnounceCommand;
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.commands.DefineCommand;
import org.tinkernut.apririce.commands.HashCommand;
import org.tinkernut.apririce.commands.HelpCommand;
import org.tinkernut.apririce.commands.JoinCommand;
import org.tinkernut.apririce.commands.LogCommand;
import org.tinkernut.apririce.commands.NickCommand;
import org.tinkernut.apririce.commands.NickServCommand;
import org.tinkernut.apririce.commands.QuitCommand;
import org.tinkernut.apririce.commands.LeaveCommand;
import org.tinkernut.apririce.textUtils.Parser;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
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
	private boolean isUsingPassword = false;
	private final String CMD_START = "|";
	private String ircServer;
	public ConnectionManager con;
	private HashMap<String, Command> commandsMap;
	private BufferedWriter bWriter;
	private BufferedReader bReader;
	private String botName;
	public LinkedList<User> usersList;
	private File usersFile;
	private String password = "";
	//Global instance commands

	/**
	 * Class constructor
	 */
	public Bot(String server, String channel, String botName) {
		ircServer = server;
		channelName = channel;
		this.botName = botName;

		// Initialize globals		
		commandsMap = new HashMap<String, Command>();
		//Put identifier and associated command
		commandsMap.put("help", new HelpCommand());
		commandsMap.put("nickserv", new NickServCommand());
		commandsMap.put("define",new DefineCommand());
		commandsMap.put("announce", new AnnounceCommand());
		commandsMap.put("log", new LogCommand());
		commandsMap.put("quit", new QuitCommand());
		commandsMap.put("nick", new NickCommand());
		commandsMap.put("leave", new LeaveCommand());
		commandsMap.put("join", new JoinCommand());
		commandsMap.put("hash", new HashCommand());

		usersList = new LinkedList<User>();
		usersFile = new File(this.channelName + "_usersFile.txt");

		if (!usersFile.exists()) {
			try {
				usersFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("File " + usersFile.getName() + " inaccessible.");
			}
		}

		// TODO: Create storage
		// Optional nick (other than default) and password read from config file for specific channel

		File configFile = new File(channelName + "_config.txt");

		try {

			if (configFile.exists()) {
				bReader = new BufferedReader(new FileReader(configFile));

				String nick = bReader.readLine();
				password = bReader.readLine();
				isUsingPassword = true;

				if (nick == null) {
					nick = this.botName;
					System.out.println("No nick specified in " + configFile.getName() + ". Using default.");			
				}else {
					this.botName = nick;
				}

				if (password == null) {
					password = "";
					isUsingPassword = false;
					System.out.println("No password specified in " + configFile.getName() + ". Not using password.");					
				}

				con = new ConnectionManager(new Profile(this.botName));
			}else {			
				// Bot profile (nick)
				System.out.println("No " + configFile.getName() + " in directory. Creating "+ configFile.getName() +" and using default nick and password (no password by default).");
				con = new ConnectionManager(new Profile(this.botName));

				configFile.createNewFile();
			}
		} catch (IOException e) {
			System.out.println("File " + configFile.getName() + " inaccessible.");
		}

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
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
			//TODO: Rewrite user logging, saving, etc,.

			String buffer = "";
			LinkedList<User> usersFileList = new LinkedList<User>();

			try {
				bReader = new BufferedReader(new FileReader(usersFile));

				// usersFile.txt to a LinkedList; usersFileList
				while ((buffer = bReader.readLine()) != null) {
					usersFileList.add(new User(buffer));
				}

				bReader.close();

				// Step 1: Put any user in usersFile.txt into usersList
				for (User user : usersFileList) {
					usersList.add(new User(user.getNick()));
				}

				// Step 2: If channel contains any users that are not in usersList, add them.
				for (String nick : jce.getChannel().getNicks()) {
					if (!usersList.contains(new User(nick))) {
						jce.getChannel().say("New user detected, " + nick + ".");
						System.out.println("New user detected, " + nick + ".");
						usersList.add(new User(nick));
					}
				}

				// Step 3: If usersFileList and usersList are different, add new users to usersFileList and write
				bWriter = new BufferedWriter(new FileWriter(usersFile, true));
				for (User user : usersList) {
					if (!usersFileList.contains(user)) {
						usersFileList.add(user);
						bWriter.write(user.getNick());
						bWriter.newLine();
					}
				}
				bWriter.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				System.out.println("File " + usersFile.getName() + " doesn't exist... it should.");
			} catch (IOException e1) {
				System.out.println("File " + usersFile.getName() + " doesn't exist... it should.");
				e1.printStackTrace();
			}

			if (isUsingPassword) {
				jce.getSession().sayPrivate("nickserv", "identify " + password);
			}


			// User successfuly joins channel
		} else if (type == Type.JOIN) {
			JoinEvent je = (JoinEvent) e;

			// User successfuly leaves channel
		} else if (type == Type.QUIT) {
			QuitEvent qe = (QuitEvent) e;

			// Message successfuly recieved in channel
		} else if (type == Type.CHANNEL_MESSAGE) {
			// Logging.
			if (isLogging) {
				try {
					bWriter = new BufferedWriter(new FileWriter("log.txt", true));
					bWriter.write(e.getRawEventData());
					bWriter.newLine();
					bWriter.close();
				} catch (IOException e1) {
					System.out.println("Error. Could not open log file.");
				}
			}

			MessageEvent me = (MessageEvent) e;
			//Check and execute any commands
			if (me.getMessage().startsWith(CMD_START)) {
				String commandString = Parser.stripCommand(me.getMessage());

				if (commandsMap.containsKey(commandString)) {
					commandsMap.get(commandString).init(Parser.stripArguments(me.getMessage()), me, this);

					Thread thread = new Thread(commandsMap.get(commandString));
					thread.start();
				}else {
					me.getChannel().say("Not a command.");
				}
			}
			// Private message successfuly recieved
		} else if (type == Type.PRIVATE_MESSAGE) {
			// Logging.
			if (isLogging) {
				try {
					bWriter = new BufferedWriter(new FileWriter("log.txt", true));
					bWriter.write(e.getRawEventData());
					bWriter.newLine();
					bWriter.close();
				} catch (IOException e1) {
					System.out.println("Error. Could not open log file.");
				}
			}

			MessageEvent me = (MessageEvent) e;
			if (me.getMessage().startsWith(CMD_START)) {			
				//Check and execute any commands
				String commandString = Parser.stripCommand(me.getMessage());

				if (commandsMap.containsKey(commandString)) {
					commandsMap.get(commandString).initPriv(Parser.stripArguments(me.getMessage()), me, this);

					Thread thread = new Thread(commandsMap.get(commandString));
					thread.start();
				}
				else {
					me.getSession().sayPrivate(me.getNick(), "Not a command.");
				}
			}
		}
	}
}