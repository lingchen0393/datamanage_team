package com.mhfs.dao;

import com.mhfs.javabean.User;

public interface UserDao {
	// register for a new user
	boolean addUser(User user);

	// change password and return with whether it is successful
	boolean changePwd(String name, String np);

	// verify user when login, if success, return with true
	boolean verifyUser(User user);

	// detect whether the username has already existed
	boolean checkNameExist(String username);
}
