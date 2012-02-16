package org.tinkernut.apririce;

public interface Command {

    public void exec(final Bot bot, String params);

    public void execPriv(final Bot bot, User sender, String params);

}
