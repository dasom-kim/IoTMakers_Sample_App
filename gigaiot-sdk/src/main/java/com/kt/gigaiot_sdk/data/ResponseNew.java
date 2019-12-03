package com.kt.gigaiot_sdk.data;

/**
 * Updated by DASOM (2018.11.13)
 */

public class ResponseNew<T> {

    private String responseCode;
    private String message;
    private T paging;

    public ResponseNew(String responseCode, String message, T paging) {
        this.responseCode = responseCode;
        this.message = message;
        this.paging = paging;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public T getPaging() {
        return paging;
    }
}
