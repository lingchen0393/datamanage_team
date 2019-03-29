package com.mhfs.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.mhfs.javabean.User;
import com.mhfs.service.UserService;

@WebServlet("/StartServlet")
public class StartServlet extends HttpServlet {
	//private static final long serialVersionUID = 1272194200575717998L;

	private UserService userService = new UserService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		JSONObject ret = new JSONObject();
		String name = req.getParameter("username");
		String pwd = req.getParameter("password");

		User user = new User(name, pwd);
		if (action.equals("login")) {
			handleLogin(user, ret,req);
		} else if (action.equals("register")) {
			handleRegister(user, ret);
		}

		resp.getWriter().write(ret.toString());
	}

	private void handleLogin(User user, JSONObject ret, HttpServletRequest req) {
		if (userService.login(user)) {
			ret.put("ret", "0");
			ret.put("msg", "");

			HttpSession session = req.getSession();
			session.setMaxInactiveInterval(24 * 60 * 60);
			session.setAttribute("name", user.getName());
		} else {
			ret.put("ret", "1");
			ret.put("msg", "username or password error");
		}
	}

	private void handleRegister(User user, JSONObject ret) {
		if (userService.register(user)) {
			ret.put("ret", "0");
			ret.put("msg", "");
		} else {
			ret.put("ret", "1");
			ret.put("msg", "username exists");
		}
	}
}
