package com.ximingren.house.common.exception;

import com.ximingren.house.common.enums.MessageEnum;

public class ExternalServiceException extends RuntimeException{
    private MessageEnum messageEnum;

    public ExternalServiceException(MessageEnum messageEnum) {
        super(messageEnum.getMsg());
        this.messageEnum = messageEnum;
    }

    public MessageEnum getMessageEnum() {
        return messageEnum;
    }
}
