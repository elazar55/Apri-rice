package org.tinkernut.apririce.commands;

import jerklib.events.MessageEvent;
import org.tinkernut.apririce.Bot;
import org.tinkernut.apririce.User;

public interface Command {
    void exec(final Bot bot, String params, MessageEvent me);

    void execPriv(final Bot bot, User sender, String params, MessageEvent me);
}
