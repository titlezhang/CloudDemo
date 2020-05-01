package com.admin.login.service;

import com.admin.login.controller.LoginResp;
import org.springframework.stereotype.Service;

@Service
public interface ILoginService {
    LoginResp login(String loginName,String pwd);
}
