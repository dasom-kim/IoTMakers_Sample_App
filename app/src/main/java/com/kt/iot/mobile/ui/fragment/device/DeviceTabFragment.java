package com.kt.iot.mobile.ui.fragment.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.DeviceTabPageAdapter;
import com.kt.iot.mobile.ui.fragment.event.list.EventListFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class DeviceTabFragment extends Fragment {
    private final String TAG = DeviceTabFragment.class.getSimpleName();
    private ArrayList<Fragment> mArrayFragments = new ArrayList<>();
    private String[] mArrayPageTitles = {"Event", "Log"};
    private EventListFragment mEventListFragment;
    private DeviceNew mDevice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "device Tab Fragment created!");

        View view = inflater.inflate(R.layout.fragment_device_tab, null);

        mEventListFragment = new EventListFragment();
        mArrayFragments.add(mEventListFragment);

        DeviceTabPageAdapter adapter = new DeviceTabPageAdapter(getFragmentManager(), mArrayPageTitles, mArrayFragments);

        ViewPager pager = view.findViewById(R.id.device_pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = view.findViewById(R.id.device_tab_indicator);
        indicator.setViewPager(pager);

        pager.setCurrentItem(0);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy()!!");

        getActivity().getSupportFragmentManager().beginTransaction().remove(mEventListFragment).commit();
    }

    public void setDevice(final DeviceNew device) {
        if (device != null) {
            mDevice = device;

            if(mEventListFragment != null){
                mEventListFragment.setDevice(mDevice);
            }
        }
    }

    public void refresh() {
        if(mEventListFragment != null) {
            mEventListFragment.refresh();
        }
    }
}
