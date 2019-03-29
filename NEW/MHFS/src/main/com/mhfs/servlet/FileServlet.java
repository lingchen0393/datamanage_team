package com.mhfs.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mhfs.javabean.File;
import com.mhfs.service.FileService;

@WebServlet("/system/FileServlet")
public class FileServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;

	private FileService fileService = new FileService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		JSONArray ret = new JSONArray();
		if (action.equals("search")) {
			String username = req.getParameter("username");
			String keyword = req.getParameter("keyword");
			handleSearch(ret, username, keyword);
		} else if (action.equals("priSearch")) {
			String name = (String) req.getSession().getAttribute("name");
			handlePriSearch(ret, name);
		}else if (action.equals("trashSearch")) {
			String username = req.getParameter("username");
			String keyword = req.getParameter("keyword");
			ret=handleTrashSearch( username,  keyword);
		
		} 
		else if (action.equals("download")) {
			String fname = req.getParameter("fname");
			String pri = req.getParameter("pri");
			JSONObject jo = new JSONObject();
			System.out.println("pri is: "+pri);
			if(pri.equals("0")) { 							// public files download requires lock
				System.out.println("gaining lock ");
				boolean flag = fileService.gainlock(fname);
			if(flag){
				File file = fileService.download(fname, Integer.parseInt(pri));
				jo.put("fName", file.getName());
				jo.put("fContent", file.getContent());
				jo.put("ret", "0");
				jo.put("msg", "download success");
				//release lock
				flag = fileService.releaselock(fname);
				if(!flag) {
					jo.put("ret", "1");
					jo.put("msg", "fail to release lock");
				}
			}
			else {
				jo.put("ret", "1");
				jo.put("msg", "fail to gain lock");
				System.out.println("fail to gain lock");
			}
			
			
			}
			else {									//private file need no lock
				File file = fileService.download(fname, Integer.parseInt(pri));
				jo.put("fName", file.getName());
				jo.put("fContent", file.getContent());
				jo.put("ret", "0");
				jo.put("msg", "download success");
			}
			resp.getWriter().write(jo.toString());
			return;
		} else if (action.equals("upload")) {
			String fName = req.getParameter("fname");
			String fContent = req.getParameter("fcontent");
			System.out.println(fContent);
			String pri = req.getParameter("pri");
			JSONObject jsonObject = new JSONObject();
			//search for existance ??
			//if exist already, whether lock is available?
			if(fileService.existFile(fName,Integer.parseInt(pri))) {
				if(pri.equals("0")) {														//public area,need lock
					boolean flag = fileService.gainlock(fName);
					if(flag) {
						System.out.println("gian lock for update!");
						File file = new File((String) req.getSession().getAttribute("name"), fName, fContent,
							Integer.parseInt(pri));
						flag = fileService.movetoTrash(fName, Integer.parseInt(pri));
					
						if (flag) {
							flag = fileService.updateFile(file);
							if(flag) {
								jsonObject.put("ret", "2");
								jsonObject.put("msg", "Update success! The previous version is in bin");
							}
							else {
								jsonObject.put("ret", "1");
								jsonObject.put("msg", "update failed but success move to trash");
							}
						
						} 
						else {
							jsonObject.put("ret", "1");
							jsonObject.put("msg", "Update failed and not move to trash");
						}
						//release lock
						flag = fileService.releaselock(fName);
						if(!flag) {
							jsonObject.put("ret", "1");
							jsonObject.put("msg", "update success but fail to release lock");
						}
					}
					else {
						jsonObject.put("ret", "1");
						jsonObject.put("msg", "Fail to gain lock");
					}
					
					
				}
				else {																//private area, no need for lock
					File file = new File((String) req.getSession().getAttribute("name"), fName, fContent,
						Integer.parseInt(pri));
					boolean flag = fileService.movetoTrash(fName, Integer.parseInt(pri));
					if (flag) {
						flag = fileService.updateFile(file);
						if(flag) {
							jsonObject.put("ret", "2");
							jsonObject.put("msg", "Update success! The previous version is in bin");
						}
						else {
							jsonObject.put("ret", "1");
							jsonObject.put("msg", "Update failed but move to trash");
						}
					
					}
					else {
						jsonObject.put("ret", "1");
						jsonObject.put("msg", "update failed but success move to trash");
					}
			}
				
			}
								//if not exist yet,upload new file directly
			else {
				File file = new File((String) req.getSession().getAttribute("name"), fName, fContent,
					Integer.parseInt(pri));
			boolean flag = fileService.addFile(file);
			
			if (flag) {
				jsonObject.put("ret", "0");
				jsonObject.put("msg", "");
			} else {
				jsonObject.put("ret", "1");
				jsonObject.put("msg", "Upload Failed");
			}
			 
			}
			resp.getWriter().write(jsonObject.toString());
			return;
		} else if (action.equals("delete")) {
			String fname = req.getParameter("fname");
			String pri = req.getParameter("pri");
			String uName = (String) req.getSession().getAttribute("name");
			JSONObject jsonObject = new JSONObject();
			if(pri.equals("0")) {												//public file need lock
				boolean flag = fileService.gainlock(fname);
			
				if(flag) {
					flag = fileService.movetoTrash(fname, Integer.parseInt(pri));
					if(flag) {
						flag = fileService.deleteFile(fname, Integer.parseInt(pri));
						if(flag) {
							jsonObject.put("ret", "0");
							jsonObject.put("msg", "move to trash");
							
						}
						else {
							jsonObject.put("ret", "1");
							jsonObject.put("msg", "delete failed");
							flag = fileService.releaselock(fname);
							if(!flag) {
								System.out.println("flag is :??"+flag);
								jsonObject.put("ret", "1");
								jsonObject.put("msg", "Move to bin but Fail to release lock");
							}
						}
					}
					else {
						jsonObject.put("ret", "1");
						jsonObject.put("msg", "move to trash failed");
					}
					
				}
				else {
					jsonObject.put("ret", "1");
					jsonObject.put("msg", "Fail to gain lock");
				}
				
			}
			else {													//private file delete, no need for lock
				boolean flag = fileService.movetoTrash(fname, Integer.parseInt(pri));
				if(flag) {
					flag = fileService.deleteFile(fname, Integer.parseInt(pri));
					if(flag) {
						jsonObject.put("ret", "0");
						jsonObject.put("msg", "move to trash");
					}
					else {
						jsonObject.put("ret", "1");
						jsonObject.put("msg", "delete failed");
					}
				}
				else {
					jsonObject.put("ret", "1");
					jsonObject.put("msg", "move to trash failed");
				}
			}
			
			resp.getWriter().write(jsonObject.toString());
			return;
		}else if (action.equals("recover")) {
			String fName = req.getParameter("fname");
			String pri = req.getParameter("pri");
			String fID = req.getParameter("id");
			JSONObject jsonObject = new JSONObject();
			
			if(fileService.existFile(fName, Integer.parseInt(pri))) {   // if the file existed
				if(pri.equals("0")) {				//public file recover need both lock
					boolean flag =fileService.gainlock(fName);
					if(flag) {					//gain public lock
						flag =fileService.gainTrashlock(fID);
						if(flag) {				//gain trash lock
							flag = fileService.deleteFile(fName, Integer.parseInt(pri));
							if(flag) {
								flag = fileService.recoverFile(fID);
								if(flag) {
									jsonObject.put("ret", "0");
									jsonObject.put("msg", "Rollback success");
								}
								else {
									jsonObject.put("ret", "1");
									jsonObject.put("msg", "Rollback failed");
									System.out.println("releasing trash lock for "+fID);
									//release trash lock 
									flag = fileService.releaseTrashlock(fID);
									if(!flag) {
										jsonObject.put("ret", "1");
										jsonObject.put("msg", "fail to release trash lock");
									}
								}
							}
							else {
								jsonObject.put("ret", "1");
								jsonObject.put("msg", "delete failed");
							}
							
							
							//release public lock
							flag = fileService.releaselock(fName);
							if(!flag) {
								jsonObject.put("ret", "1");
								jsonObject.put("msg", "fail to release public lock");
							}
				
					}			//end of gain trash lock	
						else {				//fail to gain trash lock, need to release public lock
							jsonObject.put("ret", "1");
							jsonObject.put("msg", "fail to gain trash lock");
							
							//release public lock
							flag = fileService.releaselock(fName);
							if(!flag) {
								jsonObject.put("ret", "1");
								jsonObject.put("msg", "release public lock fail to gain trash lock");
							}
						}
			   }
					else {
					jsonObject.put("ret", "1");
					jsonObject.put("msg", "fail to gain public lock");
				}
					}
				else {							//private file only need trash lock
					boolean flag = fileService.gainTrashlock(fID);
					if(flag) {			// gain trash lock
						flag = fileService.deleteFile(fName, Integer.parseInt(pri));
						if(flag) {
							flag = fileService.recoverFile(fID);
							if(flag) {
								jsonObject.put("ret", "0");
								jsonObject.put("msg", "Rollback success");
							}
							else {
								jsonObject.put("ret", "1");
								jsonObject.put("msg", "Rollback failed");
								//release trash lock 
								flag = fileService.releaseTrashlock(fID);
								if(!flag) {
									jsonObject.put("ret", "1");
									jsonObject.put("msg", "fail to release trash lock");
								}
							}
						}
						else {
							jsonObject.put("ret", "1");
							jsonObject.put("msg", "delete failed");
						}
						
						
					}
					else {				//gain trash lock failed
						jsonObject.put("ret", "1");
						jsonObject.put("msg", "fail to gain trash lock");
					}
				}
			}
			
		
			else {							//no  to-be-recovered file exist
				boolean flag = fileService.gainTrashlock(fID);
				if(flag) {				//gain trash lock, no need for public lock
					flag = fileService.recoverFile(fID);
					if(flag) {				
							jsonObject.put("ret", "0");
							jsonObject.put("msg","recover success!");
							
							//release lock
							if(pri.equals("0")) {	   		//prevent pri from being default value "1"							
							 	flag = fileService.releaselock(fName);
							 	if(!flag) {
									jsonObject.put("ret", "1");
									jsonObject.put("msg", "rollback success but fail to release lock");
								}
						}
					}
					else {				//recover failed, need release trash lock
						jsonObject.put("ret", "1");
						jsonObject.put("msg", "recover failed");
						//release trash lock 
						flag = fileService.releaseTrashlock(fID);
						if(!flag) {
							jsonObject.put("ret", "1");
							jsonObject.put("msg", "fail to release trash lock");
						}
					}
			}
				else {
					jsonObject.put("ret", "1");
					jsonObject.put("msg", "fail to gain trash lock");
				
				}
				
			}
			
			resp.getWriter().write(jsonObject.toString());
			return;
		
		}else if (action.equals("totallyDel")) {
			String fID = req.getParameter("id");
			JSONObject jsonObject = new JSONObject();
			boolean flag = fileService.gainTrashlock(fID);
			if(flag) {		//gain trash lock
				flag = fileService.totallyDel(fID);
				if(flag) {
					jsonObject.put("ret", "0");
					jsonObject.put("msg","Totally Delete!");
				}
				else {
					jsonObject.put("ret", "1");
					jsonObject.put("msg","Totally Delete failed!");
					flag = fileService.releaseTrashlock(fID);
					if(!flag) {		//release trash lock
						jsonObject.put("ret", "1");
						jsonObject.put("msg","fail to release trash lock");
					}
				}
				
			}
			else {		 	//fail to gain trash lock
				jsonObject.put("ret", "1");
				jsonObject.put("msg","fail to gain trash lock");
			}
				resp.getWriter().write(jsonObject.toString());
				return;
		
		}
		resp.getWriter().write(ret.toString());
	}

	private void handlePriSearch(JSONArray ret, String name) {
		List<File> files = fileService.searchBy(name);
		for (File file : files) {
			JSONObject jo = new JSONObject();
			//jo.put("id", file.getId());
			jo.put("uName", file.getuName());
			jo.put("fName", file.getName());
			jo.put("fContent", file.getContent());
			ret.put(jo);
		}
	}

	private void handleSearch(JSONArray ret, String username, String keyword) {
		List<File> files = fileService.searchBy(username, keyword);
		for (File file : files) {
			JSONObject jo = new JSONObject();

			jo.put("uName", file.getuName());
			jo.put("fName", file.getName());
			jo.put("fContent", file.getContent());
			ret.put(jo);
		}
	}
	
	public JSONArray	 handleTrashSearch( String username, String keyword) {
		 return fileService.searchTrash(username, keyword);
	
	}
}
