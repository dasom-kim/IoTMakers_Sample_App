package com.kt.gigaiot_sdk.data;

import java.util.ArrayList;

public class DeviceApiResponseNew extends Response {
    private Paging pagings;
    private ArrayList<DeviceNew> devices;

    public DeviceApiResponseNew(String responseCode, String message, Paging pagings, ArrayList<DeviceNew> devices) {
        super(responseCode, message);
        this.pagings = pagings;
        this.devices = devices;
    }

    public Paging getPagings() {
        return pagings;
    }

    public ArrayList<DeviceNew> getDevices() {
        return devices;
    }
}
