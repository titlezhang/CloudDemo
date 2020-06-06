package com.admin.login.service;

import com.admin.login.controller.LoginResp;

public interface ILoginService {
    LoginResp login(String loginName, String pwd);
}
