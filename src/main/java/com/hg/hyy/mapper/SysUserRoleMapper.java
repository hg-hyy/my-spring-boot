package com.hg.hyy.mapper;

import org.apache.ibatis.annotations.Select;
import com.hg.hyy.model.SysUserRole;

import java.util.List;

/**
 * @author zenghui
 * @date 2020-05-20
 */

public interface SysUserRoleMapper {
    @Select("SELECT * FROM sys_user_role WHERE user_id = #{userId}")
    List<SysUserRole> listByUserId(Integer userId);
}