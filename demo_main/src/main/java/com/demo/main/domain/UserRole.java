package com.demo.main.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_user_role")
@Data
public class UserRole {
    @TableField("uid")
    private Integer userId;
    @TableField("rid")
    private Integer roleId;
}
