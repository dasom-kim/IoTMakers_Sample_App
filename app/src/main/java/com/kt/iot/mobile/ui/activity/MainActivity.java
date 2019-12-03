package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.PushApiNew;
import com.kt.gigaiot_sdk.SvcTgtNewApiNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.PushApiResponse;
import com.kt.gigaiot_sdk.data.PushTypePair;
import com.kt.gigaiot_sdk.data.SvcTgtApiResponseNew;
import com.kt.gigaiot_sdk.data.SvcTgtNew;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.dashboard.DashboardFragment;
import com.kt.iot.mobile.ui.fragment.device.DeviceFragment;
import com.kt.iot.mobile.ui.fragment.device.list.DeviceCardAdapter;
import com.kt.iot.mobile.ui.fragment.device.list.DeviceListFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Common;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DeviceCardAdapter.OnDeviceListSelectedListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private DeviceNew mDevice;
    private ArrayList<SvcTgtNew> mArraySvcTgt;
    private String mbrId;
    private static final String TAG = MainActivity.class.getSimpleName();
    private DeviceListFragment deviceListFragment;
    private final int REQ_LOGOUT = 0;
    public static final String EXTRA_MEMBER_ID = "member_id";
    public static final String EXTRA_MEMBER_SEQ = "member_seq";
    private ImageView mSearchImage;
    private ImageView homemenu;
    private TextView accountTextView;
    private RelativeLayout mainFragment;
    private LinearLayout loading;

    // back키 제어
    boolean isClick = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isClick = false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        // IntroActivity로 부터 사용자의 ID가 전달됨
        mbrId = getIntent().getStringExtra(EXTRA_MEMBER_ID);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.desc_open_drawer, R.string.desc_close_drawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mSearchImage = findViewById(R.id.search_area);
        mSearchImage.setOnClickListener(this);
        homemenu = findViewById(R.id.main_home);
        homemenu.setOnClickListener(this);
        Util.setStatusBarColor(this, Color.parseColor("#27284d"));
        accountTextView = findViewById(R.id.main_account_id);
        mainFragment = findViewById(R.id.fragment_container);
        loading = findViewById(R.id.data_loading);

        setUserName();
        initDashboardFragment();
    }

    public void setUserName(){
        accountTextView.setText(mbrId);
    }

    private void initDashboardFragment () {
        DashboardFragment dashboardFragment = new DashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dashboardFragment).commit();
        dashboardRefresh();
    }

    private void dashboardRefresh() {
        initializeDeviceListFragment();

        new GetSvcTgtTask().getSvcTgt();
    }

    private void initializeDeviceListFragment() {
        mainFragment.setVisibility(View.VISIBLE);
        deviceListFragment = new DeviceListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, deviceListFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != this.RESULT_OK){
            return;
        }

        switch(requestCode){
            case REQ_LOGOUT: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            break;
        }

    }

    public void onDeviceSelected(int position, DeviceNew device) {
        mDevice = device;
        DeviceFragment fragment = (DeviceFragment) getSupportFragmentManager().findFragmentById(R.id.device_fragment);

        if (fragment != null) {             //Tablet
            fragment.setDevice(device);
        } else {                            //mobile
            Gson gson = new Gson();
            String strDevice = gson.toJson(mDevice);

            Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
            intent.putExtra(DeviceActivity.EXTRA_DEVICE, strDevice);

            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isClick) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                Snackbar.make(mDrawerLayout, "뒤로가기를 다시 누르면 앱이 종료됩니다", Snackbar.LENGTH_LONG).show();
                isClick = true;
                handler.sendEmptyMessageDelayed(0, 2000);
            }
        } else {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.finishAffinity(this);
            } else {
                finishAffinity();
                System.runFinalization();
            }

            System.exit(0);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        // 각 메뉴 클릭시 이뤄지는 이벤트

        switch (id) {
            case R.id.navigation_item_guide: {
                intent = new Intent(getApplicationContext(), GuideActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.navigation_item_policy: {
                intent = new Intent(getApplicationContext(), PolicyActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.navigation_item_account: {
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
            break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;

        switch (id) {
            case R.id.search_area : {
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }

            break;

            case R.id.main_home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }

            break;
        }
    }

    private class GetSvcTgtTask {
        protected void getSvcTgt() {
            Log.d(TAG, "get Svctgt!");
            SvcTgtNewApiNew svcTgtApi = new SvcTgtNewApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<SvcTgtApiResponseNew>() {
                        @Override
                        public void onStart() {
                            loading.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onDoing(SvcTgtApiResponseNew response) {
                            if (response.getSvcTgts() != null && response.getSvcTgts().size() > 0) {
                                loading.setVisibility(View.GONE);
                                mArraySvcTgt = response.getSvcTgts();

                                Log.d(TAG, "Svctgt size : " + mArraySvcTgt.size());

                                new PushSessionRegTask().pushSessionReg();
                            } else {
                                ApplicationPreference.getInstance().setPrefAccountId("");

                                Snackbar.make(mDrawerLayout, R.string.check_user, Snackbar.LENGTH_INDEFINITE).show();

                                new PushSessionDeleteTask().pushSessionDelete();
                            }
                        }

                        @Override
                        public void onFail() {
                            loading.setVisibility(View.GONE);
                            Snackbar.make(mDrawerLayout, R.string.internet_fail, Snackbar.LENGTH_LONG)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dashboardRefresh();
                                        }
                                    }).show();
                        }
                    });

            svcTgtApi.getNewSvcTgtSeqList();
        }
    }

    private class PushSessionRegTask {
        protected void pushSessionReg() {
            ArrayList<PushTypePair> pushTypePairs = new ArrayList<>();
//            pushTypePairs.add(new PushTypePair(mArraySvcTgt.get(0).getSvcTgtSeq(), PushApiNew.PUSH_MSG_TYPE_COLLECT));
//            pushTypePairs.add(new PushTypePair(mArraySvcTgt.get(0).getSvcTgtSeq(), PushApiNew.PUSH_MSG_TYPE_OUTBREAK));
            pushTypePairs.add(new PushTypePair(mArraySvcTgt.get(0).getSequence(), PushApiNew.PUSH_MSG_TYPE_COLLECT));
            pushTypePairs.add(new PushTypePair(mArraySvcTgt.get(0).getSequence(), PushApiNew.PUSH_MSG_TYPE_OUTBREAK));

            PushApiNew pushApi = new PushApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<PushApiResponse>() {
                        @Override
                        public void onStart() {
                            Log.d(TAG, "Push session Register starting");
                        }

                        @Override
                        public void onDoing(PushApiResponse pushApiResponse) {

                        }

                        @Override
                        public void onFail() {
                            Log.d(TAG, "Push session Register is failed");
                        }
                    });

            pushApi.gcmSessionRegistration(ApplicationPreference.getInstance().getPrefAccountMbrSeq(),
                    Common.CLIENT_ID,
                    ApplicationPreference.getInstance().getPrefGcmRegId(),
                    pushTypePairs);
        }
    }

    private class PushSessionDeleteTask {
        protected void pushSessionDelete() {
            PushApiNew pushApi = new PushApiNew(
                    ApplicationPreference.getInstance().getPrefAccessToken(),
                    new APICallback<PushApiResponse>() {
                        @Override
                        public void onStart() {
                            Log.d(TAG, "Push session Delete starting");
                        }

                        @Override
                        public void onDoing(PushApiResponse pushApiResponse) {

                        }

                        @Override
                        public void onFail() {
                            Log.d(TAG, "Push session Delete is failed");
                        }
                    });

            pushApi.gcmSessionDelete(ApplicationPreference.getInstance().getPrefGcmRegId());
        }
    }
}
