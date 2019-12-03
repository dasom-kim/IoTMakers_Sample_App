package com.kt.gigaiot_sdk.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ceoko on 15. 3. 24..
 */
public class DeviceStatus {

    @SerializedName("svcTgtSeq")
    private String svcTgtSeq;

    @SerializedName("spotDevSeq")
    private String spotDevSeq;

    @SerializedName("spotDevId")
    private String spotDevId;

    @SerializedName("devModelSeq")
    private String devModelSeq;

    @SerializedName("status")
    private String status;

    public String getSvcTgtSeq() {
        return svcTgtSeq;
    }

    public void setSvcTgtSeq(String svcTgtSeq) {
        this.svcTgtSeq = svcTgtSeq;
    }

    public String getSpotDevSeq() {
        return spotDevSeq;
    }

    public void setSpotDevSeq(String spotDevSeq) {
        this.spotDevSeq = spotDevSeq;
    }

    public String getSpotDevId() {
        return spotDevId;
    }

    public void setSpotDevId(String spotDevId) {
        this.spotDevId = spotDevId;
    }

    public String getDevModelSeq() {
        return devModelSeq;
    }

    public void setDevModelSeq(String devModelSeq) {
        this.devModelSeq = devModelSeq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "svcTgtSeq='" + svcTgtSeq + '\'' +
                "spotDevSeq='" + spotDevSeq + '\'' +
                "spotDevId='" + spotDevId + '\'' +
                "devModelSeq='" + devModelSeq + '\'' +
                "status='" + status + "'" +
                '}';
    }
}
