package com.demo.main.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@TableName("user")
@Data
@Builder
public class User {
    @TableId
    private Integer id;
    private String name;
    private String address;
    private Integer status;
}
