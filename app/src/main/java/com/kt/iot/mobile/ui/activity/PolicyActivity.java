package com.kt.iot.mobile.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.ui.fragment.policy.PolicyFragment;
import com.kt.iot.mobile.utils.Util;

/*
 * Updated by DASOM
 * 사용자 약관 화면
 */
public class PolicyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_common);

        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        PolicyFragment fragment = new PolicyFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, fragment).commit();
        Util.setStatusBarColor(this, Color.parseColor("#27284d"));
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
    public void onBackPressed() {
        finish();
    }
}
