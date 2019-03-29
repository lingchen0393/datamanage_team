package com.mhfs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mhfs.dao.Sql;
import com.mhfs.dao.UserDao;
import com.mhfs.javabean.User;
import com.mhfs.util.JdbcUtil;

public class UserDaoImpl implements UserDao {

	@Override
	public boolean addUser(User user) {
		boolean res = false;

		Connection connection = JdbcUtil.getConection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(Sql.ADD_USER);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getPwd());
			if (preparedStatement.executeUpdate() != 0) {
				res = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					connection.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}
		}
		return res;
	}

	@Override
	public boolean changePwd(String name, String np) {
		boolean res = false;

		Connection connection = JdbcUtil.getConection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(Sql.CHANGE_PWD);
			preparedStatement.setString(1, np);
			preparedStatement.setString(2, name);
			if (preparedStatement.executeUpdate() == 1) {
				res = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					connection.close();
				}
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException e) {
			}
		}
		return res;
	}

	@Override
	public boolean verifyUser(User user) {
		boolean res = false;

		Connection connection = JdbcUtil.getConection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(Sql.VERIFY_USER);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getPwd());
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				res = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					connection.close();
				}
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException e) {
			}
		}
		return res;
	}

	@Override
	public boolean checkNameExist(String username) {
		boolean res = false;

		Connection connection = JdbcUtil.getConection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(Sql.CHECK_NAME_EXIST);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				res = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					connection.close();
				}
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException e) {
			}
		}
		return res;
	}

}
