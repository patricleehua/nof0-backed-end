package com.patriclee.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {
    /**
     * 异常码
     */
    private String errorCode;
    /**
     * 异常信息
     */
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        super(baseExceptionInterface.getErrorMessage());
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}
