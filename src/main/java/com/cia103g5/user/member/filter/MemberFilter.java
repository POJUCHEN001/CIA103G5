package com.cia103g5.user.member.filter;

import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.member.model.MemberVO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
