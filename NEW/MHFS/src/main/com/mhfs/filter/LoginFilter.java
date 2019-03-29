package com.mhfs.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter extends HttpFilter {
	//private static final long serialVersionUID = -8934184552394226490L;

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession httpSession = request.getSession(false);
		if (httpSession != null && httpSession.getAttribute("name") != null) {
			chain.doFilter(request, response);
		}
		response.sendRedirect("../start.html");
	}
}
