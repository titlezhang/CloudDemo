package com.admin.login.controller;

import com.admin.common.UrlDefine;
import com.admin.login.service.ILoginService;
import com.common.response.BaseResp;
import com.common.response.RespCode;
import com.common.utils.StrUtils;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录相关业务入口
 */
@Controller
@RequestMapping("")
public class LoginController {
    @Autowired
    ILoginService loginService;

    /**
     * 后台登录接口
     * @param loginReq
     * @return
     */
    @RequestMapping(name = UrlDefine.LOGIN,method = RequestMethod.POST)
    @ResponseBody
    public LoginResp login(@RequestBody LoginReq loginReq){
        //参数校验
        if(StringUtils.isEmpty(loginReq.getLoginName())){
            return new LoginResp(RespCode.PARAM_ERROR.getCode(),"账号不能为空");
        }
        if(StringUtils.isEmpty(loginReq.getPwd())){
            return new LoginResp(RespCode.PARAM_ERROR.getCode(),"密码不能为空");
        }
        return loginService.login(loginReq.getLoginName(),loginReq.getPwd());
    }
}
