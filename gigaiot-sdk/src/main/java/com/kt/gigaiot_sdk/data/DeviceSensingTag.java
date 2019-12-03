package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ceoko on 15. 3. 24..
 */
public class DeviceSensingTag {

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("dataType")
    private String dataType;

    @SerializedName("individual")
    private String individual;

    @SerializedName("unit")
    private String unit;

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

    @Override
    public String toString() {
        return "DeviceSensingTag{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", dataType='" + dataType + '\'' +
                ", individual='" + individual + '\'' +
                ", unit=" + unit + "'" +
                '}';
    }
}
