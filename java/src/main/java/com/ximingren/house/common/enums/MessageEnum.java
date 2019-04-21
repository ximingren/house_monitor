package com.ximingren.house.common.enums;

public enum MessageEnum {
    SUCCESS(0, "成功"),
    SYSTEM_ERROR(-9999, "系统错误"),
    DATABASE_DATE_ERROR(-9001, "数据库日期插件异常"),
    DATABASE_PAGE_ERROR(-9002, "数据库分页插件异常"),
    JSON_PARSE_ERROR(-9003, "json解析异常"),
    PARAMS_EMPTY_ERROR(-9004, "请求参数不能为空：{0}"),
    DATABASE_ERROR(-9005, "数据库异常"),
    SERVICE_NOT_EXIST(-9006, "请求接口不存在"),
    TOKEN_INVALID(-9007, "无效的授权token"),
    QUERY_EMPTY(-9008, "查询数据不存在"),
    DELETE_EMPTY(-9009, "删除数据不存在"),
    ;

    MessageEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
