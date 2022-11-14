package com.demo.main.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@TableName("sys_user")
@Data
@Builder
public class User implements Serializable {
    @TableId
    private Integer id;
    private String username;
    private String password;
    private Integer status;
    public User() {
    }

    public User(Integer id, String username, String password, Integer status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
    }
}
