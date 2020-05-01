package com.common.response;

public enum RespCode {
    /**
     * 网络访问，业务操作成功
     */
    SUCCESS("0000","操作成功"),
    FAILURE("0001","失败，未知原因"),
    PARAM_ERROR("0002","入参有误");
    private String code;
    private String desc;
    RespCode(String code,String desc){
        this.code=code;
        this.desc=desc;
    }
    public String getCode(){
        return this.code;
    }
    public String getDesc(){
        return this.desc;
    }

}
