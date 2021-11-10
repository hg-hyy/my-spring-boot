package com.hg.hyy.mapper;

import org.apache.ibatis.annotations.Select;
import com.hg.hyy.model.SysUser;

/**
 * @author zenghui
 * @date 2020-05-20
 */

public interface SysUserMapper {
    @Select("SELECT * FROM sys_user WHERE name = #{name}")
    SysUser selectByName(String name);
}
