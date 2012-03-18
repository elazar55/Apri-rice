package org.tinkernut.apririce;

public class User {
	public int warnings = 0;
	private String nick;
	Rank rank = Rank.Standard;
	
	public User(String nick) {
		this.nick = nick;
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

enum Rank{
	Admin, Standard;
}