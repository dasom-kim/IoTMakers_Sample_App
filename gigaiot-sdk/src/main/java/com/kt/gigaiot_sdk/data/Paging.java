package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("total")
    String total;

    @SerializedName("offset")
    String offset;

    @SerializedName("limit")
    String limit;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
