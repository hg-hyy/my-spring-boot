package com.hg.hyy.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface MyAccessService {

    public boolean myUri(HttpServletRequest request, Authentication authentication);
}
