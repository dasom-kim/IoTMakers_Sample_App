package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ceoko on 15. 4. 9..
 */
public class SvcTgtNew {

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

    @SerializedName("type")
    String type;

    @SerializedName("status")
    String status;

    @SerializedName("creator")
    String creator;

    @SerializedName("createdOn")
    String createdOn;

    @SerializedName("memberSequence")
    String memberSequence;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getMemberSequence() {
        return memberSequence;
    }

    public void setMemberSequence(String memberSequence) {
        this.memberSequence = memberSequence;
    }

    @Override
    public String toString() {
        return "SvcTgtNew{" +
                "districtCode='" + districtCode + '\'' +
                ", themeCode='" + themeCode + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", sequence='" + sequence + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", creator='" + creator + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", memberSequence='" + memberSequence + '\'' +
                '}';
    }
}
