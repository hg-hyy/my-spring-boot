package com.hg.hyy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MySuccessHandler implements AuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(MyFailureHandler.class);

    private String forwardUrl;

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }


    public MySuccessHandler() {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(forwardUrl), () -> "'" + forwardUrl + "'is not a valid forward url");
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.getRequestDispatcher(forwardUrl).forward(httpServletRequest, httpServletResponse);
        httpServletResponse.sendRedirect(forwardUrl);
    }
}
