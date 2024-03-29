package com.kt.iot.mobile.ui.fragment.device;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.TagStrmApiNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.device.control.DeviceCtrlListFragment;
import com.kt.iot.mobile.ui.fragment.device.detail.DeviceDetailFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;

/*
 * Updated by DASOM
 * 디바이스 상세 화면 Fragment
 */
public class DeviceFragment extends Fragment {
    private static final String TAG = DeviceFragment.class.getSimpleName();
    //private Device mDevice;
    private DeviceNew mDevice;
    private DeviceDetailFragment mDeviceDetailFragment;
    private DeviceTabFragment mDeviceTabFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.w(TAG, "onCreateView!!");

        View view = inflater.inflate(R.layout.fragment_device, container, false);

        if (mDeviceDetailFragment == null) {
            mDeviceDetailFragment = new DeviceDetailFragment();
        }

        if (view.findViewById(R.id.device_detail_fragment_container) != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.device_detail_fragment_container, mDeviceDetailFragment).commit();
        }

        if(mDeviceTabFragment == null){
            Log.w(TAG, "onCreateView() mDeviceTabFragment == null");
            mDeviceTabFragment = new DeviceTabFragment();
        }

        if (view.findViewById(R.id.device_bottom_fragment_container) != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.device_bottom_fragment_container, mDeviceTabFragment).commit();
        }

        setDevice(mDevice);

        return view;
    }

    public void setDevice(final DeviceNew device) {
        if (device != null) {
            mDevice = device;
            if (mDeviceDetailFragment != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        TagStrmApiNew tagStrmApi = new TagStrmApiNew(
                                ApplicationPreference.getInstance().getPrefAccessToken(),
                                new APICallback<TagStrmApiResponse>() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onDoing(final TagStrmApiResponse response) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (mDevice != null) {

                                                    mDevice.setTagStrmList(response.getTagStrms());

                                                    if (mDeviceDetailFragment != null) {
                                                        mDeviceDetailFragment.setDevice(mDevice);
                                                    }

                                                    if(mDeviceTabFragment != null){
                                                        mDeviceTabFragment.setDevice(mDevice);
                                                    }


                                                    refresh();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFail() {

                                    }
                                });
                        //tagStrmApi.getTagStrmList(device.getSpotDevId());
                        tagStrmApi.getTagStrmList(device.getId());

                    }
                }).start();

            }
        }
    }

    public void refresh() {
        if (mDeviceDetailFragment != null) {
            mDeviceDetailFragment.refresh();
        }

        if(mDeviceTabFragment != null){
            mDeviceTabFragment.refresh();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_ctrl :
                if(mDevice != null) {
                    DeviceCtrlListFragment fragment = new DeviceCtrlListFragment();
                    fragment.setDevice(mDevice);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.device_bottom_fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, "onPause!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy!!");
        
        getActivity().getSupportFragmentManager().beginTransaction().remove(mDeviceTabFragment).commit();
        getActivity().getSupportFragmentManager().beginTransaction().remove(mDeviceDetailFragment).commit();
    }
}
