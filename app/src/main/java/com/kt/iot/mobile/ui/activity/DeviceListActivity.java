package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.data.SvcTgtNew;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.device.list.DeviceCardAdapter;
import com.kt.iot.mobile.ui.fragment.device.list.DeviceListFragment;
import com.kt.iot.mobile.utils.Util;

/*
 * Updated by DASOM
 * 디바이스 목록 화면 (메인)
 */
public class DeviceListActivity extends AppCompatActivity implements DeviceCardAdapter.OnDeviceListSelectedListener{
    private final String TAG = DeviceListActivity.class.getSimpleName();
    public final static String EXTRA_SVCTGT = "svctgt";
    Gson mGson;
    SvcTgtNew mSvcTgt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_common);

        mGson = new Gson();
        mSvcTgt = mGson.fromJson(getIntent().getStringExtra(EXTRA_SVCTGT), SvcTgtNew.class);

        Log.w(TAG, mSvcTgt.toString());

        DeviceListFragment fragment = new DeviceListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();
        Util.setStatusBarColor(this, Color.parseColor("#27284d"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.device_list_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeviceSelected(int position, DeviceNew device) {

        if(device != null) {
            String strDevice = mGson.toJson(device);
            String strSvcTgt = mGson.toJson(mSvcTgt);

            Intent intent = new Intent(DeviceListActivity.this, DeviceActivity.class);
            intent.putExtra(DeviceActivity.EXTRA_DEVICE, strDevice);
            intent.putExtra(DeviceActivity.EXTRA_SVCTGT, strSvcTgt);

            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
