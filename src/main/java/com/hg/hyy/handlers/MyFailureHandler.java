package com.hg.hyy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyFailureHandler implements AuthenticationFailureHandler {

    private final Logger log = LoggerFactory.getLogger(MyFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        log.error("httpServletRequest");
    }
}
