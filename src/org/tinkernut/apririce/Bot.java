package org.tinkernut.apririce;

import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.Class;
import org.tinkernut.apririce.commands.Command;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;
import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.JoinEvent;
import jerklib.events.MessageEvent;
import jerklib.events.QuitEvent;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;

// TODO: Refactor certain operations to methods

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
	private final int maximumFlood = 10;
	private long lastMessageTime;
	private final int floodDeclineInterval = 2000;
	private final boolean isFloodChecking = true;
	private final boolean doesCheckFloodAdmins = false;
	//Global instance commands

	/*
	 * Class constructor
	 */
	public Bot(String server, String channel, String botName) {
		// Initialize globals		
		ircServer = server;
		channelName = channel;
		this.botName = botName;
		commandsMap = new HashMap<String, Command>();
		lastMessageTime = 0;
		
		// Count number of commands for for loop if directory exists
		File commandsDirectory = new File("src\\org\\tinkernut\\apririce\\commands\\");
		// If directory doesn't exist
		if (!commandsDirectory.exists()) {
			System.out.println("There are no commands installed. Create or get some commands and place them in src\\org\\tinkernut\\apririce\\commands\nExiting.");
			commandsDirectory.mkdirs();
			// Exist completely if doesn't exist
			System.exit(1);
		}
		// And if it does exist...
		int numberOfCommands = commandsDirectory.listFiles().length;
		// Each file into commandsFilesArray array
		File commandsFilesArray[] = new File("src\\org\\tinkernut\\apririce\\commands\\").listFiles();
		String className = "";

		for (int i = 0; i < numberOfCommands; i++) {
			try {
				className = commandsFilesArray[i].getName().substring(0, commandsFilesArray[i].getName().indexOf("."));
				if (className.equals("Command")) {
					continue;
				}

				// File string to Command type
				@SuppressWarnings("rawtypes")
				Class c = Class.forName("org.tinkernut.apririce.commands." + className);

				if (className.contains("Command")) {					
					commandsMap.put(className.substring(0, className.indexOf("Command")).toLowerCase(), (Command) c.newInstance());
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Internal error; class " + className +" not found.");
				e.printStackTrace();
			} catch (InstantiationException e) {
				System.out.println("Internal error; class " + className +" not found.");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.out.println("Internal error; class " + className +" not found.");
				e.printStackTrace();
			} catch (ClassCastException e) {
				System.out.println(className + " does not extend Command. Skipping.");
				continue;
			}
		}

		// TODO: Create storage

		// Optional nick (other than default) and password read from config file for specific channel
		File configFile = new File(channelName + "_config.txt");

		try {

			if (configFile.exists()) {
				bReader = new BufferedReader(new FileReader(configFile));

				String buffer = "";
				String nick = "";

				while ((buffer = bReader.readLine())!= null) {
					if (!buffer.startsWith("||")) {
						nick = buffer;
						password = bReader.readLine();						
					}
				}
				isUsingPassword = true;

				if (nick.equals("")) {
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
				bReader.close();
			}else {			
				// Bot profile (nick)
				System.out.println("No " + configFile.getName() + " in directory. Creating "+ configFile.getName() +" and using default nick and password (no password by default).");
				con = new ConnectionManager(new Profile(this.botName));

				configFile.createNewFile();
				bWriter = new BufferedWriter(new FileWriter(configFile));
				bWriter.write("||Nick on first line, password on next line. Leave next line blank if no password.");
				bWriter.close();
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
			/*
			 * User logging, etc,.
			 */
			usersList = new LinkedList<User>();                               // Instantiate new usersList
			usersFile = new File(this.channelName + "_usersFile.txt");        // Instantiate usersFile with name: ChannelName + "_usersFile.txt"

			if (!usersFile.exists()) {
				try {
					usersFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("File " + usersFile.getName() + " inaccessible.");
				}
			}

			String buffer = "";
			LinkedList<User> usersFileList = new LinkedList<User>();          // Temporary usersFile LinkedList; usersFileList to represent initial usersFile txt file 

			try {
				bReader = new BufferedReader(new FileReader(usersFile));      // new bufferedReader to usersFile txt file, as to convert to usersFileList

				// usersFile.txt to a LinkedList; usersFileList
				while ((buffer = bReader.readLine()) != null) {					
					Rank rank = null;
					String nick = "";
					
					if (buffer.contains(" ")) {
						nick = buffer.substring(0, buffer.indexOf(" "));						
					} else {						
						nick = buffer;						
					}
					
					// Check if rank admin exists in txt file. If so, rank = admin, else, rank = standard
					if (buffer.substring(buffer.indexOf(" ") + 1).equalsIgnoreCase("admin")) {
						rank = Rank.Admin;
					} else {
						rank = Rank.Standard;
					}
					
					usersFileList.add(new User(nick, rank));
				}

				bReader.close();

				// Step 1: Put any user in usersFile.txt(usersFileList) into usersList
				for (User user : usersFileList) {
					usersList.add(user);
				}

				// Step 2: If channel contains any users that are not in usersList, add them.
				for (String nick : jce.getChannel().getNicks()) {
					if (!usersList.contains(new User(nick, Rank.Standard)) || !usersList.contains(new User(nick, Rank.Admin))) {
						jce.getChannel().say("New user detected, " + nick + ".");
						System.out.println("New user detected, " + nick + ".");
						usersList.add(new User(nick, Rank.Standard));
					}
				}

				// Step 3: If usersFileList and usersList are different, add new users to usersFileList and write
				bWriter = new BufferedWriter(new FileWriter(usersFile, true));
				for (User user : usersList) {
					if (!usersFileList.contains(user)) {
						usersFileList.add(user);
						bWriter.write(user.getNick() + " " + user.rank.toString());
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

			// User successfully joins channel
		} else if (type == Type.JOIN) {
			JoinEvent je = (JoinEvent) e;
			if (usersList.contains(new User(je.getNick()))) {                         // Check if joined user exists in usersList
				je.getChannel().say("I know you =.=");
			} else {
				je.getChannel().say("New user detected, " + je.getNick() + ".");      // Add to usersFile and write to file if not
				usersList.add(new User(je.getNick()));
				
				try {
					bWriter = new BufferedWriter(new FileWriter(usersFile, true));
					bWriter.write(je.getNick() + " " + Rank.Standard.toString());
					bWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println(usersFile.getName() + " inaccessable.");
				}
			}

			// User successfully leaves channel
		} else if (type == Type.QUIT) {
			QuitEvent qe = (QuitEvent) e;

			// Message successfully received in channel
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
			// TODO: Per user flood control
			// Flood checking
			if (isFloodChecking ) {
				// Check current message time against floodDeclineInterval. Increment user's floodCounter if too low.
				if (System.currentTimeMillis() - lastMessageTime < floodDeclineInterval) {
					if (doesCheckFloodAdmins) {
						try {
							usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Standard))).floodCounter++;
						} catch (IndexOutOfBoundsException e1) {
							usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Admin))).floodCounter++;
						}
					} else {
						usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Standard))).floodCounter++;
					}
				} else {    // else decrement user's floodCounter
					if (usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Standard))).floodCounter > 0) {
						usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Standard))).floodCounter--;
					}
				}
				
				TextBuffer.addAndDisplay(me.getNick() + "'s Flood counter is: " + usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Standard))).floodCounter, me);
				// If user's floodCounter hit maximumFlood
				if (usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Standard))).floodCounter == maximumFlood) {
					me.getChannel().kick(me.getNick(), "Stop flooding.");
					usersList.get(usersList.indexOf(new User(me.getNick(), Rank.Standard))).floodCounter = 0;
				}
				
				lastMessageTime = System.currentTimeMillis();
			}
			
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
			// Private message successfully received
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