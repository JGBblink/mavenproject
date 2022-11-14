package com.demo.main.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@TableName("sys_role")
public class Role {

    @TableId
    private String id;
    @TableField("role_name")
    private String roleName;
    @TableField("role_desc")
    private String roleDesc;
}
