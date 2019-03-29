package com.mhfs.service;

import com.mhfs.dao.UserDao;
import com.mhfs.dao.impl.UserDaoImpl;
import com.mhfs.javabean.User;

public class UserService {
	private static String register = "";
	private static String changePwd = "";

	UserDao userDao = new UserDaoImpl();

	public boolean login(User user) {
		return userDao.verifyUser(user);
	}

	public boolean register(User user) {
	//	synchronized (register) {
			if (!userDao.checkNameExist(user.getName())) {
				return userDao.addUser(user);
			}
			return false;
	//	}
	}

	public boolean changePwd(String name, String op, String np) {
	//	synchronized (changePwd) {
			if (userDao.verifyUser(new User(name, op))) {
				return userDao.changePwd(name, np);
			}
			return false;
	//	}

	}
}
