package com.kt.iot.mobile.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.GigaIotOAuthNew;
import com.kt.gigaiot_sdk.data.GiGaIotOAuthResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.iot.mobile.android.R;
import com.kt.iot.mobile.utils.ApplicationPreference;
import com.kt.iot.mobile.utils.Common;
import com.kt.iot.mobile.utils.Util;

import java.io.IOException;

/*
 * Updated by DASOM
 * 로그인 화면
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private final String TAG = LoginActivity.class.getSimpleName();
    private EditText mEtId, mEtPw;
    private GoogleCloudMessaging mGcm;
    private String mbrSeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtId = findViewById(R.id.et_login_id);
        mEtPw = findViewById(R.id.et_login_pw);

        ImageView ivLogin = findViewById(R.id.iv_login_bt);
        ivLogin.setOnClickListener(this);

        Util.setStatusBarColor(this, Color.WHITE);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void startMainActivity(String id, String mbrSeq) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.putExtra(MainActivity.EXTRA_MEMBER_ID, id);
        intent.putExtra(MainActivity.EXTRA_MEMBER_SEQ, mbrSeq);

        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_login_bt:
                String id = mEtId.getText().toString();
                String pw = mEtPw.getText().toString();

                if(TextUtils.isEmpty(id)) {
                    Toast.makeText(LoginActivity.this, R.string.login_id_empty, Toast.LENGTH_SHORT).show();
                    return;

                } else if(TextUtils.isEmpty(pw)) {
                    Toast.makeText(LoginActivity.this, R.string.login_pw_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                new LoginTask().login();
                break;

        }
    }

    public class LoginTask {
        ProgressDialog progressDialog;
        String id;

        protected void login() {
            id = mEtId.getText().toString();
            String pw = mEtPw.getText().toString();

            GigaIotOAuthNew gigaIotOAuth = new GigaIotOAuthNew(
                    Common.CLIENT_ID,
                    Common.CLIENT_SECRET,
                    new APICallback<GiGaIotOAuthResponse>() {
                        public void onStart() {
                            progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.common_wait), true, false);
                        }

                        public void onDoing(GiGaIotOAuthResponse response) {
                            progressDialog.dismiss();
                            progressDialog = null;

                            if (response != null) {
                                if (response.getResponseCode().equals(ApiConstants.CODE_OK)) {
                                    //if(result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)){
                                    ApplicationPreference.getInstance().setPrefAccountId(id);
                                    ApplicationPreference.getInstance().setPrefAccessToken(response.getAccessToken());
                                    ApplicationPreference.getInstance().setPrefAccountMbrSeq(response.getMbrSeq());

                                    //TODO :GCM 등록 테스트
                                    mGcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                                    //mGcm.register("772329232378");

                                    mbrSeq = response.getMbrSeq();
                                    Log.d(TAG, "member Sequence: " + mbrSeq);

                                    new LoginActivity.GetGcmRegIdTask().execute();

                                    startMainActivity(id, mbrSeq);
                                } else {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail) + response.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        public void onFail() {
                            progressDialog.dismiss();
                            progressDialog = null;

                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                        }
                    });

            // 로그인 시도
            long start = System.currentTimeMillis();
            gigaIotOAuth.loginWithPassword(id, pw);
            long end = System.currentTimeMillis();

            Log.d(TAG, "spended time of login : " + (end-start)/1000.0);
        }

    }

    private class GetGcmRegIdTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {

            String regId = null;

            try {
                //regId = mGcm.register("772329232378");
                regId = mGcm.register("371742022785");
                Log.i(TAG, "GetGcmRegIdTask regId = " + regId); //app

            } catch (IOException e) {
                e.printStackTrace();
            }

            ApplicationPreference.getInstance().setPrefGcmRegId(regId);
            return regId;
        }

    }


}
