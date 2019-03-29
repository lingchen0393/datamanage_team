package com.mhfs.dao;

import java.util.List;

import org.json.JSONArray;

import com.mhfs.javabean.File;

public interface FileDao {
	boolean addFile(File file);

	boolean deleteFile(String fname, int pri);
	
	boolean movetoTrash(String fname, int pri);
	

	boolean updateFileBy(File file);
	
	boolean recoverFile(String fID);
	
	boolean totallyDel(String fID);
	
	boolean existFile(String fname, int pri);

	List<File> getFileListBy(String uName, String keyWord);
	
	JSONArray getTrashListBy(String uName, String keyWord);

	String getUnameBy(String fname);

	List<File> priSearch(String name);

	File getFileBy(String fname, int pri);

	

	boolean gainlock(String fname);

	boolean releaselock(String fname);
	
	boolean gainTrashlock(String fID);
	
	boolean releaseTrashlock(String fID);
}
