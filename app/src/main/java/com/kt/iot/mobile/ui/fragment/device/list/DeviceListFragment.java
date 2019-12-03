package com.kt.iot.mobile.ui.fragment.device.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.DeviceNewApiNew;
import com.kt.gigaiot_sdk.data.DeviceApiResponseNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.Paging;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DeviceListFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = DeviceListFragment.class.getSimpleName();
    private ArrayList<DeviceNew> mDevices;
    private boolean mScrollOccured = false;
    private int mLastItemCount = 0;
    private RecyclerView mCardView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout deviceLoading;
    private View view;
    private int tmpDeviceOpenY, tmpDeviceUsedY;
    private RequestManager requestManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDevices = new ArrayList<>();

        view = inflater.inflate(R.layout.fragment_device_list, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        requestManager = Glide.with(getActivity());
        mAdapter = new DeviceCardAdapter(getActivity(), mDevices, requestManager);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCardView = view.findViewById(R.id.DeviceList);
        mCardView.setLayoutManager(mLayoutManager);
        mCardView.setHasFixedSize(true);

        deviceLoading = view.findViewById(R.id.device_data_loading);

        refresh();

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void refresh() {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        tmpDeviceOpenY = 0;
        tmpDeviceUsedY = 0;

        if(mDevices != null && mDevices.size() > 0){
            mDevices.clear();
            mAdapter.notifyDataSetChanged();
        }

        new GetDevListTask().getDeviceList();
    }

    @Override
    public void onRefresh() {
        refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private class GetDevListTask {
        protected void getDeviceList() {
            Log.d(TAG, "get Device List!");

            DeviceNewApiNew deviceApi = new DeviceNewApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<DeviceApiResponseNew>() {
                        @Override
                        public void onStart() {
                            deviceLoading.setVisibility(View.VISIBLE);
                            mCardView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onDoing(DeviceApiResponseNew response) {

                            if(response.getDevices() != null && response.getDevices().size() > 0){

                                Paging paging = response.getPagings();
                                Log.i(TAG, "refresh() getDevices total = " + paging.getTotal());

                                for(DeviceNew device : response.getDevices()){
                                    if (device.getPublished() != null && device.getPublished().equals(ApiConstants.boolean_true)) tmpDeviceOpenY++;
                                    if (device.getUsed() != null && device.getUsed().equals(ApiConstants.boolean_true)) tmpDeviceUsedY++;

                                    mDevices.add(device);
                                }

                                mCardView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                                if (mScrollOccured) {
                                    mCardView.scrollToPosition(mLastItemCount - 1);
                                    mScrollOccured = false;
                                }

                                Util.setDeviceOpenY(tmpDeviceOpenY);
                                Util.setDeviceUsedY(tmpDeviceUsedY);
                                Util.setDeviceCount(mDevices.size());

                                deviceLoading.setVisibility(View.GONE);
                                mCardView.setVisibility(View.VISIBLE);
                            } else {
                                Snackbar.make(mCardView, R.string.check_user, Snackbar.LENGTH_INDEFINITE).show();
                            }
                        }

                        @Override
                        public void onFail() {
                            deviceLoading.setVisibility(View.GONE);

                            Snackbar.make(mCardView, R.string.internet_fail, Snackbar.LENGTH_INDEFINITE)
                                    .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    refresh();
                                }
                            }).show();
                        }
                    });

            // 개인당 최대 10개의 디바이스만 등록 가능하므로, offset = 1만 설정해줘도 됨
            deviceApi.getNewDeviceList(1);
        }

    }
}