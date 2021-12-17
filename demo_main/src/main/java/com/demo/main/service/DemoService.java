package com.demo.main.service;


import com.demo.main.domain.User;
import java.util.List;

public interface DemoService {

    List<User> listUsers(String appName);
}
