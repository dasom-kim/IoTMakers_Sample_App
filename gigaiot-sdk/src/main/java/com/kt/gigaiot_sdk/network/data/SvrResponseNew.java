package com.kt.gigaiot_sdk.network.data;

import com.google.gson.annotations.SerializedName;
import com.kt.gigaiot_sdk.data.Paging;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 7..
 */
public class SvrResponseNew<M> {

    @SerializedName("responseCode")
    String responseCode;
    @SerializedName("message")
    String message;
    @SerializedName("paging")
    Paging paging;
    @SerializedName("data")
    ArrayList<M> data;

    public SvrResponseNew(String responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public SvrResponseNew(String responseCode, String message, ArrayList<M> data) {
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Paging getPaging() {
        return paging;
    }

    public ArrayList<M> getData() {
        return data;
    }

    public void setData(ArrayList<M> data) {
        this.data = data;
    }
}
