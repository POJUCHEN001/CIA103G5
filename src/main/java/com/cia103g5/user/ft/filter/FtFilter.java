package com.cia103g5.user.ft.filter;

import java.io.IOException;

import com.cia103g5.user.member.dto.SessionMemberDTO;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/ftAPI/*") // 攔截所有請求
public class FtFilter extends HttpFilter implements Filter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Object isLogin = request.getSession().getAttribute("isLogin");
		String URL = (String) session.getAttribute("redirectURL");
		Object ftId = request.getSession().getAttribute("ftId");
		// 是否登入
		if (isLogin != null) {
			// 是否為占卜師
			if (ftId != null) {
				chain.doFilter(request, response);
			}
		} else {
			response.sendRedirect(request.getContextPath() + "/login");
		}

	}
}
