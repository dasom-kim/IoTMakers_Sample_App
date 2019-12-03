package com.kt.iot.mobile.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Util;

public class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Util.setStatusBarColor(this, Color.WHITE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginState();
            }
        }, 2000);

    }

    private void checkLoginState(){
        String id = ApplicationPreference.getInstance().getPrefAccountId();
        String seq = ApplicationPreference.getInstance().getPrefAccountMbrSeq();

        if(id != null && id.equals("") == false){
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_MEMBER_ID, id);
            intent.putExtra(MainActivity.EXTRA_MEMBER_SEQ, seq);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
