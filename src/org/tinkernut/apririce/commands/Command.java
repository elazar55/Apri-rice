package org.tinkernut.apririce.commands;

import org.tinkernut.apririce.*;
import org.tinkernut.apririce.users.*;

public interface Command {
    public void exec(final Bot bot, String params);

    public void execPriv(final Bot bot, User sender, String params);
}
