package com.hg.hyy.interfaces.impl;

import com.hg.hyy.interfaces.MyAccess;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Component
public class MyAccessImpl implements MyAccess {
    @Override
    public boolean hasPermit(HttpServletRequest request, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(request.getRequestURI()));
    }
}

