package com.kt.gigaiot_sdk;

import android.text.TextUtils;
import android.util.Log;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.Paging;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.data.SvcTgtApiResponse;
import com.kt.gigaiot_sdk.data.SvcTgtApiResponseNew;
import com.kt.gigaiot_sdk.data.SvcTgtNew;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.network.data.SvrResponse;
import com.kt.gigaiot_sdk.network.data.SvrResponseNew;
import com.kt.gigaiot_sdk.util.Utils;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Updated by DASOM
 * service target sequence 목록 API
 */
public class SvcTgtNewApiNew {

    private final String TAG = SvcTgtNewApiNew.class.getSimpleName();

    APIList apiList;
    private APICallback<SvcTgtApiResponseNew> apiCallback;
    private String mAccessToken;

    public SvcTgtNewApiNew(String accessToken, APICallback<SvcTgtApiResponseNew> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    public void getNewSvcTgtSeqList(){

        //error handling - user access token missing
        if(TextUtils.isEmpty(mAccessToken)){
            apiCallback.onFail();
            return;
        }

        Log.d(TAG, "get New Service Target Sequence!");

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponseNew<SvcTgtNew>> call = apiList.doGetNewSvctgtSeqList(mAccessToken,1, 10);
        call.enqueue(new Callback<SvrResponseNew<SvcTgtNew>>() {
            @Override
            public void onResponse(Call<SvrResponseNew<SvcTgtNew>> call, Response<SvrResponseNew<SvcTgtNew>> response) {
                SvrResponseNew<SvcTgtNew> result = response.body();

                // OK 메세지 없는 API
                if (response.isSuccessful()) {
                    apiCallback.onDoing(new SvcTgtApiResponseNew(result.getResponseCode(), result.getMessage(), result.getPaging(), result.getData()));
                // Unauthorized
                } else if (response.code() == 401) {
                    apiCallback.onDoing(new SvcTgtApiResponseNew(null, null, null, null));
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponseNew<SvcTgtNew>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    Log.i(TAG, "Socket Time out. Please try again.");
                } else {
                    Log.i(TAG, "API error caused by " + t.getMessage());
                }

                apiCallback.onFail();
                call.cancel();
            }
        });
    }

}
