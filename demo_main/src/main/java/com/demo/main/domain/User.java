package com.demo.main.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {
    @TableId
    private Integer id;
    private String name;
    private String address;
    private Integer status;
}
