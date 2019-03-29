package com.mhfs.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.mhfs.service.UserService;

@WebServlet("/system/UserServlet")
public class UserServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;

	private UserService userService = new UserService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		JSONObject ret = new JSONObject();
		if (action.equals("changePwd")) {
			String name = (String) req.getSession().getAttribute("name");
			String op = req.getParameter("op");
			String np = req.getParameter("np");
			handleChangePwd(ret, name, op, np);
		} else if (action.equals("quit")) {
			handleQuit(ret, req);
		}

		resp.getWriter().write(ret.toString());
	}

	private void handleQuit(JSONObject ret, HttpServletRequest req) {
		HttpSession httpSession = req.getSession();
		if(httpSession!=null) {
			httpSession.setAttribute("name", null);
		}
		ret.put("ret", "0");
		ret.put("msg", "");
	}

	private void handleChangePwd(JSONObject ret, String name, String op, String np) {
		if (userService.changePwd(name, op, np)) {
			ret.put("ret", "0");
			ret.put("msg", "");
		} else {
			ret.put("ret", "1");
			ret.put("msg", "old password error");
		}
	}
}
