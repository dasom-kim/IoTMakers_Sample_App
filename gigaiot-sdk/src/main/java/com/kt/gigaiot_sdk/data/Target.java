package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

public class Target {

    @SerializedName("districtCode")
    String districtCode;

    @SerializedName("themeCode")
    String themeCode;

    @SerializedName("serviceCode")
    String serviceCode;

    @SerializedName("sequence")
    String sequence;

    @SerializedName("name")
    String name;

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getThemeCode() {
        return themeCode;
    }

    public void setThemeCode(String themeCode) {
        this.themeCode = themeCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
