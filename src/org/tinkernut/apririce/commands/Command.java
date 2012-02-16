package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public interface Command {
    void exec(final Bot bot, String params);

    void execPriv(final Bot bot, User sender, String params);
}
