
package org.tinkernut.apririce.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.tinkernut.apririce.Rank;
import org.tinkernut.apririce.User;
import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;

//TODO: Rewrite this
public class UserCommand extends Command{
	private final String helpText = "Available arguments: ";

	@Override
	void exec() {
		if (params.isEmpty()) {
			TextBuffer.addAndDisplay(helpText, me);
		} else if (Parser.getArgument(params, 1).equalsIgnoreCase("promote")) {
			if (!Parser.getArgument(params, 2).isEmpty()) {
				if (bot.usersList.contains(new User(Parser.getArgument(params, 2)))) {
					if (bot.usersList.get(bot.usersList.indexOf(new User(Parser.getArgument(params, 2)))).rank != Rank.Admin) {
						bot.usersList.get(bot.usersList.indexOf(new User(Parser.getArgument(params, 2)))).rank = Rank.Admin;
						TextBuffer.addAndDisplay(Parser.getArgument(params, 2) + " has been promoted to Admin!", me);

						try {
							BufferedWriter bWriter = new BufferedWriter(new FileWriter(bot.usersFile));

							for (User user : bot.usersList) {
								bWriter.write(user.getNick() + " " + user.rank.toString());
								bWriter.newLine();
							}
							bWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println(bot.usersFile.getName() + " inaccessable.");
						}
					} else {
						TextBuffer.addAndDisplay("User is already admin.", me);
					}
				} else {
					TextBuffer.addAndDisplay("User does not exist.", me);
				}
			} else {
				TextBuffer.addAndDisplay("No user entered to promote.", me);
			}

		}
	}

	@Override
	void execPriv() {

	}
}