package org.tinkernut.apririce;

public class User {
	public int warnings = 0;
	public String nick;
	
	public User(String nick) {
		this.nick = nick;
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
