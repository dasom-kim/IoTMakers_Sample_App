package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

public class AuthenticationCertificate {

    @SerializedName("sequence")
    String sequence;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
