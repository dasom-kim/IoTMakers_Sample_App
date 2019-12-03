package com.kt.gigaiot_sdk.data;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 4. 2..
 */
public class DeviceStatusResponse extends Response {

    private ArrayList<DeviceStatus> deviceStatuses;

    public DeviceStatusResponse(String responseCode, String message, ArrayList<DeviceStatus> deviceStatuses) {
        super(responseCode, message);
        this.deviceStatuses = deviceStatuses;
    }

    public ArrayList<DeviceStatus> getDeviceStatuses() {
        return deviceStatuses;
    }
}
