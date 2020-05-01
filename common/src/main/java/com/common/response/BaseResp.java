package com.common.response;

public class BaseResp {
    private String respCode;
    private String desc;
    public BaseResp(String respCode,String desc){
            this.desc=desc;
            this.respCode=respCode;
    }
    public BaseResp(RespCode respCode){
        this.desc=respCode.getDesc();
        this.respCode=respCode.getCode();
    }
    public static BaseResp getBaseResp(String respCode,String desc){
        return new BaseResp(respCode,desc);
    }
    public static BaseResp getBaseResp(RespCode respCode){
        return  new BaseResp(respCode);
    }

    public String getRespCode() {
        return respCode;
    }

    public BaseResp setRespCode(String respCode) {
        this.respCode = respCode;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public BaseResp setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
