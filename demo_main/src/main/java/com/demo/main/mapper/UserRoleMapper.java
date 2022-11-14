package com.demo.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.main.domain.Role;
import com.demo.main.domain.User;
import com.demo.main.domain.UserRole;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("select sr.id,sr.role_name,sr.role_desc\n" +
            "from sys_user su\n" +
            "left join sys_user_role sur on su.id = sur.uid\n" +
            "left join sys_role sr on sr.id = sur.rid\n" +
            "where su.username = #{userName}")
    List<Role> selectRolesBYUserName(@Param("userName") String userName);
}
