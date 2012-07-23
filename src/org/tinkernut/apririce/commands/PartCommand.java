package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.textUtils.Parser;

public class PartCommand extends Command {

	@Override
	void exec() {
		if (params.isEmpty()) {
			me.getChannel().part("Part channel by command");
		} else {
			me.getChannel().part(Parser.stripArguments(params));
		}
		bot.con.quit();
	}

	@Override
	void execPriv() {
		exec();
	}

}
