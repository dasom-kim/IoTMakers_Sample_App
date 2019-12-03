package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

public class Categories {

    @SerializedName("code")
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
