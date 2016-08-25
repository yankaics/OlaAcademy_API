package com.kzsrm.utils;

import javax.servlet.*;
import javax.servlet.http.*;

public class SessionCounter implements HttpSessionListener {
	private ServletContext context = null;// servlet上下文

	public void sessionCreated(HttpSessionEvent event) {
		// 因为创建session没有动作,所以这个方法就可以不写了
		System.out.println("create session  start ...");
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		// 监听session,当session过期后,就转道登陆页面
		System.out.println("session distory  ...");
		if (context == null)
			storeInServletContext(event);
	}

	private void storeInServletContext(HttpSessionEvent event) {
		System.out.println("不知道干啥的");
		HttpSession session = event.getSession();
		context = session.getServletContext();
		context.setAttribute("sessionCounter", this);
	}
}