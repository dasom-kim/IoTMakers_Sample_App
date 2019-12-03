package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.TagStrmApiNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.SvcTgtNew;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.DeviceTabPageAdapter;
import com.kt.iot.mobile.ui.fragment.device.control.DeviceCtrlListFragment;
import com.kt.iot.mobile.ui.fragment.event.list.EventListFragment;
import com.kt.iot.mobile.ui.fragment.rawdata.RawdataGraphWebViewFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.ModifyDeviceMgr;
import com.kt.iot.mobile.utils.Util;
import java.util.ArrayList;

public class DeviceActivity extends AppCompatActivity {

    private final String TAG = DeviceActivity.class.getSimpleName();
    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_SVCTGT = "svctgt";
    private DeviceNew mDevice;
    private ArrayList<Fragment> mArrayFragments = new ArrayList<>();
    private String[] mArrayPageTitles = {"Event", "Log", "Control"};
    private RawdataGraphWebViewFragment mRawdataGraphWebViewFragment;
    private EventListFragment mEventListFragment;
    private DeviceCtrlListFragment mDeviceCtrlListFragment;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        String strDevice = getIntent().getStringExtra(EXTRA_DEVICE);

        Gson gson = new Gson();
        mDevice = gson.fromJson(strDevice, DeviceNew.class);

        Toolbar toolbar = findViewById(R.id.device_tab_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleText = findViewById(R.id.action_bar_title_device_detail);
        titleText.setText(mDevice.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Util.setStatusBarColor(this, Color.parseColor("#27284d"));

        initDeviceTab();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mDevice != null && ModifyDeviceMgr.getModifyDevice() != null){
            //최근 수정된 디바이스 정보와 spotDevSeq가 같은경우
            if(mDevice.getSequence().equals(ModifyDeviceMgr.getModifyDevice().getSequence())){
                mDevice.setName(ModifyDeviceMgr.getModifyDevice().getName());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_ctrl: {
                Log.w(TAG, "onOptionsItemSelected!! id = R.id.action_ctrl");
                Gson gson = new Gson();
                Intent intent = new Intent(this, DeviceSettingActivity.class);
                String strDevice = gson.toJson(mDevice);
                intent.putExtra(DeviceSettingActivity.EXTRA_DEVICE, strDevice);
                startActivity(intent);
                return true;
            }

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void initDeviceTab(){
        tabLayout = findViewById(R.id.tabs);

        mEventListFragment = new EventListFragment();
        mRawdataGraphWebViewFragment = new RawdataGraphWebViewFragment();
        mDeviceCtrlListFragment = new DeviceCtrlListFragment();

        mArrayFragments.add(mEventListFragment);
        mArrayFragments.add(mRawdataGraphWebViewFragment);
        mArrayFragments.add(mDeviceCtrlListFragment);

        for (int i = 0; i < mArrayPageTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mArrayPageTitles[i]));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        DeviceTabPageAdapter adapter = new DeviceTabPageAdapter(getSupportFragmentManager(), mArrayPageTitles, mArrayFragments);

        final ViewPager pager = findViewById(R.id.device_pager_scroll);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);

        pager.setCurrentItem(0);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        new GetTagStrmListTask().getTagStrmList();
    }

    private class GetTagStrmListTask {
        protected void getTagStrmList() {
            TagStrmApiNew tagStrmApi = new TagStrmApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<TagStrmApiResponse>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDoing(TagStrmApiResponse response) {
                            Log.i("In GetTagStrmListTask3", mDevice.toString());

                            if (response != null && response.getResponseCode().equals(ApiConstants.CODE_OK)) {
                                mDevice.setTagStrmList(response.getTagStrms());

                                mRawdataGraphWebViewFragment.setDevice(mDevice);
                                mRawdataGraphWebViewFragment.refresh();

                                mEventListFragment.setDevice(mDevice);
                                mEventListFragment.refresh();

                                mDeviceCtrlListFragment.setDevice(mDevice);
                                mDeviceCtrlListFragment.refresh();
                            }
                        }

                        @Override
                        public void onFail() {

                        }
                    });

            tagStrmApi.getTagStrmList(mDevice.getId());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
