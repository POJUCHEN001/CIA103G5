package com.cia103g5.user.member.filter;

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

@WebFilter(urlPatterns = {"/membersAPI/*", "reservations"}) // 攔截所有請求
public class MemberFilter extends HttpFilter implements Filter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
        Object isLogin = request.getSession().getAttribute("isLogin");
        // 是否登入
        if(isLogin != null && sessionMember != null) {
            chain.doFilter(request,response);
        } else {
            response.sendRedirect("request.getContextPath() + /login");
        }

    }

}
