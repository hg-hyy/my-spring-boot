package com.hg.hyy.handlers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws ServletException, IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setHeader("Content-Type", "application/json");
        httpServletResponse.setCharacterEncoding("utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write("{\"status\":\"403\",\"msg\":\"没有权限访问,请联系管理员\"}");
        writer.flush();
        writer.close();
    }
}

