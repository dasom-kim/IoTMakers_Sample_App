package com.kt.gigaiot_sdk.data;

import java.util.ArrayList;

/**
 * Updated by DASOM (2018.11.16)
 */
public class DeviceOpenResponse extends Response {

    private int total;
    private int page;
    private int rowNum;
    private ArrayList<DeviceOpen> deviceOpenList;

    public DeviceOpenResponse(String responseCode, String message, ArrayList<DeviceOpen> deviceOpenList) {
        super(responseCode, message);
        this.deviceOpenList = deviceOpenList;
    }

    public DeviceOpenResponse(String responseCode, String message, int total, int page, int rowNum, ArrayList<DeviceOpen> deviceOpenList) {
        super(responseCode, message);
        this.total = total;
        this.page = page;
        this.rowNum = rowNum;
        this.deviceOpenList = deviceOpenList;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getRowNum() {
        return rowNum;
    }

    public ArrayList<DeviceOpen> getDeviceOpenList() {
        return deviceOpenList;
    }
}
