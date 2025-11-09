package com.patriclee.exception;

public interface BaseExceptionInterface {

    /**
     * 获取异常码
     * @return
     */
    String getErrorCode();

    /**
     * 获取异常信息
     * @return
     */
    String getErrorMessage();
}
