package com.hg.hyy.service.impl;

import com.hg.hyy.service.MyAccessService;
import com.hg.hyy.service.SysUserDetailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Service
public class MyAccessServiceImpl implements MyAccessService {
    @Override
    public boolean myUri(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if(principal instanceof SysUserDetailService sysUserDetailService){
            Collection<? extends GrantedAuthority> authorities = sysUserDetailService.getAuthorities();
            return authorities.contains(new SimpleGrantedAuthority(request.getRequestURI()));
        }

        return false;
    }
}


