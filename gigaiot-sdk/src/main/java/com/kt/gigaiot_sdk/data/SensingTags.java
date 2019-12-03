package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

public class SensingTags {

    @SerializedName("code")
    String code;

    @SerializedName("name")
    String name;

    @SerializedName("type")
    String type;

    @SerializedName("dataType")
    String dataType;

    @SerializedName("individual")
    String individual;

    @SerializedName("unit")
    String unit;

    // Device API에서 응답하는 SensingTags를 위함

    @SerializedName("value")
    String value;

    @SerializedName("updatedOn")
    String updatedOn;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
