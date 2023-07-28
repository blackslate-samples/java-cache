package io.blackslate.caffeine;

public class UserRepo {

	public static User getUser(String userId) {
		
		return new User(userId, "UserFromDB", "USER");
		
	}
	
}
