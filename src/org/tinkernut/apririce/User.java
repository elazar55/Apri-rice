package org.tinkernut.apririce;
/**
 * Class which IRC users are based on. When comparing, only nicks are compared; overrides equals.
 * @author elazar55
 *
 */
public class User {
	public int warnings = 0;
	private String nick;
	public Rank rank = null;
	protected int floodCounter = 0;
	
	/*
	 * Class Constructors
	 */
	public User(String nick, Rank rank) {
		this.nick = nick;
		this.rank = rank;
	}
	
	public User(String nick) {
		this.nick = nick;
		this.rank = Rank.Standard;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String string) {
		nick = string;
	}
	
	@Override
	public boolean equals(Object object) {
		User user = (User) object;
		if (!this.nick.equals(user.nick)) {
			return false;
		}
		return true;
	}
}