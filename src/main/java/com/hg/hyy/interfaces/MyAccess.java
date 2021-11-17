package com.hg.hyy.interfaces;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface MyAccess {
    boolean hasPermit(HttpServletRequest request, Authentication authentication);
}
