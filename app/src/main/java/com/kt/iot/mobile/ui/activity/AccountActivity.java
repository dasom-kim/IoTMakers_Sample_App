package com.kt.iot.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.settings.AccountFragment;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

/*
 * Updated by DASOM
 * 사용자 정보 화면
 */
public class AccountActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = AccountActivity.class.getSimpleName();
    private ImageView logoutIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_account);

        Toolbar toolbar = findViewById(R.id.account_setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        logoutIv = findViewById(R.id.logout);
        logoutIv.setOnClickListener(this);

        AccountFragment fragment = new AccountFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_account_fragment_container, fragment).commit();
        Util.setStatusBarColor(this, Color.parseColor("#27284d"));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.logout:

                ApplicationPreference.getInstance().setPrefAccountId("");
                new AccountFragment.PushSessionDeleteTask().pushSessionDelete();

                this.setResult(this.RESULT_OK);

                Intent intent = new Intent(this, LoginActivity.class);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);

                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
