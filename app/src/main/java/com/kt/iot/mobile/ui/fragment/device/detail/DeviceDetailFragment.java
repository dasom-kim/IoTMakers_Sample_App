package com.kt.iot.mobile.ui.fragment.device.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.iot.mobile.GiGaIotApplication;
import com.kt.iot.mobile.android.R;

/*
 * Updated by DASOM
 * 디바이스 상세 화면 Fragment
 */
public class DeviceDetailFragment extends Fragment {
    private final String TAG = DeviceDetailFragment.class.getSimpleName();
    private DeviceNew device;
    private View mView;
    private TextView mTvDeviceName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_device_detail, container, false);

        mTvDeviceName = mView.findViewById(R.id.title);
        mTvDeviceName.setTypeface(GiGaIotApplication.getDefaultTypeFace());

        refresh();
        return mView;
    }

    public void setDevice(DeviceNew device) {
        this.device = device;
    }

    public void refresh() {
        if (mView != null) {
            if (device != null) {
                if (device.getName() != null) {
                    if (mTvDeviceName != null) {
                        mTvDeviceName.setText(device.getName());
                    }
                }

            }
        }
    }

}
