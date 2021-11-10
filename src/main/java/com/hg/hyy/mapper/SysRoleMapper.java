package com.hg.hyy.mapper;

import org.apache.ibatis.annotations.Select;
import com.hg.hyy.model.SysRole;

/**
 * @author zenghui
 * @date 2020-05-20
 */

public interface SysRoleMapper {
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(Integer id);
}