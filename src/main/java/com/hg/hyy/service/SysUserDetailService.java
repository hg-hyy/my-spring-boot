package com.hg.hyy.service;

import com.hg.hyy.mapper.SysRoleMapper;
import com.hg.hyy.mapper.SysUserMapper;
import com.hg.hyy.mapper.SysUserRoleMapper;
import com.hg.hyy.model.SysRole;
import com.hg.hyy.model.SysUser;
import com.hg.hyy.model.SysUserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author hyy
 * @date 2021-11-18
 */
@Service
public class SysUserDetailService implements UserDetailsService {
    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final Collection<GrantedAuthority> authorities = new ArrayList<>();

    public SysUserDetailService(SysUserMapper sysUserMapper, SysRoleMapper sysRoleMapper,
            SysUserRoleMapper sysUserRoleMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 从数据库中取出用户信息
        SysUser user = sysUserMapper.selectByName(username);

        // 判断用户是否存在
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 添加权限
        List<SysUserRole> userRoles = sysUserRoleMapper.listByUserId(user.getId());
        for (SysUserRole userRole : userRoles) {
            SysRole role = sysRoleMapper.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        // List<GrantedAuthority> auths =
        // AuthorityUtils.commaSeparatedStringToAuthorityList("admins,ROLE_sale");

        // 返回UserDetails实现类
        return new User(user.getName(), user.getPassword(), authorities);

    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
