package com.kt.iot.mobile.utils;

import com.kt.gigaiot_sdk.data.DeviceNew;

/*
 * Updated by DASOM
 * 변경된 디바이스 setting
 */
public class ModifyDeviceMgr {

    public static DeviceNew modifyDevice;

    public static DeviceNew getModifyDevice() {
        return modifyDevice;
    }

    public static void setModifyDevice(DeviceNew modifyDevice) {
        ModifyDeviceMgr.modifyDevice = modifyDevice;
    }
}
