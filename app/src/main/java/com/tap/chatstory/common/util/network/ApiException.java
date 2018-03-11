package com.tap.chatstory.common.util.network;

/**
 * Created by lebron on 2017/8/1.
 */

public class ApiException extends RuntimeException {
    private static String message;

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
