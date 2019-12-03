package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("sequence")
    String sequence;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("type")
    String type;

    @SerializedName("protocolType")
    String protocolType;

    @SerializedName("bindingType")
    String bindingType;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getBindingType() {
        return bindingType;
    }

    public void setBindingType(String bindingType) {
        this.bindingType = bindingType;
    }
}
