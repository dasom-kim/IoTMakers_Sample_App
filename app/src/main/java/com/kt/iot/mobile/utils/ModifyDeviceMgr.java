package com.kt.iot.mobile.utils;

import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.DeviceNew;

public class ModifyDeviceMgr {

//    public static Device modifyDevice;
    public static DeviceNew modifyDevice;

//    public static Device getModifyDevice() {
//        return modifyDevice;
//    }

    public static DeviceNew getModifyDevice() {
        return modifyDevice;
    }

//    public static void setModifyDevice(Device modifyDevice) {
//        ModifyDeviceMgr.modifyDevice = modifyDevice;
//    }

    public static void setModifyDevice(DeviceNew modifyDevice) {
        ModifyDeviceMgr.modifyDevice = modifyDevice;
    }
}
