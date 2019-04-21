package com.ximingren.house.common.dto;

import com.ximingren.house.common.enums.MessageEnum;

public class ResponseDto<T> {
    private Integer code;
    private String msg;
    private T data;

    public ResponseDto(MessageEnum messageEnum) {
        this.code = messageEnum.getCode();
        this.msg = messageEnum.getMsg();
        if (messageEnum.getCode() == MessageEnum.PARAMS_EMPTY_ERROR.getCode()) {
            String message = MessageEnum.PARAMS_EMPTY_ERROR.getMsg();
            MessageEnum.PARAMS_EMPTY_ERROR.setMsg(message.split(":")[0] + ": {0}");
        }
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
