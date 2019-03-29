package com.mhfs.dao;

public class Sql {
	/**
	 * UserDao sql
	 */
	public static final String ADD_USER = "insert into users values (?, ?)";
	public static final String VERIFY_USER = "select * from users where name = ? and pwd = ?";
	public static final String CHECK_NAME_EXIST = "select * from users where name = ?";
	public static final String CHANGE_PWD = "update users set pwd = ? where name = ?";

	/**
	 * filedao sql
	 */
	public static final String SEARCH_FILE = "select u_name, name from file where u_name like ? and name like ? and private = 0";
 	public static final String SEARCH_FILE_BY_NAME = "select * from file where name = ? and private = ? ";
	public static final String SEARCH_PRI_FILE = "select  u_name, name from file where u_name = ? and private = 1";
	public static final String ADD_FILE = "insert into file values (?, ?, ?, ?, 0)";
	public static final String GET_UNMAE_BY_FNAME = "select u_name from file where name = ?";
	public static final String DELETE_FILE = "delete from file where name = ? and private = ?";
	public static final String UPDATE_FILE_BY_FNAME = "update file set u_name = ? , content = ?  where private = ? and name = ?";
    public static final String MOVE_TO_TRASH = "INSERT INTO trash(u_name,name,content,private) SELECT u_name,name,content,private FROM file where name = ? and private = ?";
	public static final String TOTALLY_DEL = "delete from trash where id = ?";
	public static final String RECOVER = "INSERT INTO file(u_name,name,content,private) SELECT u_name,name,content,private FROM trash where id = ? ";
    public static final String SEARCH_TRASH = "select id, u_name, name , private from trash where u_name like ? and name like ?";
    /**
	 * common sql
	 */
	//public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    
    public static final String CHECK_LOCK = "select lockval from file where name = ? and private = 0";
    public static final String GAIN_LOCK = "Update file set lockval = 1 where name =? and private = 0";
    public static final String RELEASE_LOCK = "Update file set lockval = 0 where name =? and private = 0";
    public static final String CHECK_TRASHLOCK = "select lockval from trash where id = ?";
    public static final String GAIN_TRASHLOCK = "Update trash set lockval = 1 where id = ?";
    public static final String RELEASE_TRASHLOCK = "Update trash set lockval = 0 where id = ?";

}
