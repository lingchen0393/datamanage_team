package com.mhfs.service;

import java.util.List;

import org.json.JSONArray;

import com.mhfs.dao.FileDao;
import com.mhfs.dao.impl.FileDaoImpl;
import com.mhfs.javabean.File;

public class FileService {
	private FileDao fileDao = new FileDaoImpl();

	public List<File> searchBy(String username, String keyword) {
		return fileDao.getFileListBy(username, keyword);
	}

	public JSONArray searchTrash(String username, String keyword){
		return fileDao.getTrashListBy(username, keyword);
	}
	public boolean addFile(File file) {
		return fileDao.addFile(file);
	}

	public boolean deleteFile(String fname, int pri) {
			return fileDao.deleteFile(fname, pri);
	}

	public boolean movetoTrash(String fname, int pri) {
		return fileDao.movetoTrash(fname, pri);
	}
	
	public boolean recoverFile(String fID) {
		return fileDao.recoverFile(fID);
	}
	
	public boolean totallyDel(String fID) {
		return fileDao.totallyDel(fID);
	}
	
	public boolean updateFile(File file) {
		return fileDao.updateFileBy(file);
	}
	public boolean existFile(String fname, int pri) {
		return fileDao.existFile(fname, pri);
	}

	public List<File> searchBy(String name) {
		
		return fileDao.priSearch(name);
	}

	public File download(String fname, int pri) {
		return fileDao.getFileBy(fname, pri);
	}
	
	
	public boolean gainlock(String fname) {
		return fileDao.gainlock(fname);
	}

	public boolean releaselock(String fname) {
		return fileDao.releaselock(fname);
	}
	
	public boolean gainTrashlock(String fID) {
		return fileDao.gainTrashlock(fID);
	}
	
	public boolean releaseTrashlock(String fID) {
		return fileDao.releaseTrashlock(fID);
	}
}
