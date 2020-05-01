package com.admin.login.controller;

import com.common.response.BaseResp;
import com.common.response.RespCode;

import java.util.List;

public class LoginResp extends BaseResp {
    private String loginName;
    private String nickName;
    private List<String> roleName;
    private String headPicUrl;
    private String mobilePhone;
    private String token;

    public LoginResp(String respCode, String desc) {
        super(respCode, desc);
    }

    public LoginResp(RespCode respCode) {
        super(respCode);
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getRoleName() {
        return roleName;
    }

    public void setRoleName(List<String> roleName) {
        this.roleName = roleName;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
