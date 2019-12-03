package com.kt.gigaiot_sdk;

import android.util.Log;

import com.kt.gigaiot_sdk.Callback.ImageCallback;
import com.kt.gigaiot_sdk.error.AccesTokenNullException;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.network.data.SvrResponse;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ceoko on 15. 3. 23..
 * Updated by DASOM
 * 디바이스 이미지 API
 */
public class DeviceImage {

    private final String TAG = DeviceImage.class.getSimpleName();

    APIList apiList;
    private ImageCallback imageCallback;

    private String mAccessToken;

    public DeviceImage(String accessToken, ImageCallback imageCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.imageCallback = imageCallback;
    }

    public void getDeviceImage(String svcTgtSeq, String spotDevSeq) {
        //error handling - user access token missing
        checkAccessToken();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse> call = apiList.getImageBase64(svcTgtSeq, spotDevSeq, mAccessToken);
        call.enqueue(new Callback<SvrResponse>() {
            @Override
            public void onResponse(Call<SvrResponse> call, Response<SvrResponse> response) {
                SvrResponse result = response.body();

                Log.d(TAG, "image base64 result: " + result.getData());

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    imageCallback.onDoing(result.getData().toString());
                } else {
                    imageCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    Log.i(TAG, "Socket Time out. Please try again.");
                } else {
                    Log.i(TAG, "API error caused by " + t.getMessage());
                }

                imageCallback.onFail();
                call.cancel();
            }
        });
    }

    private void checkAccessToken(){
        if(mAccessToken == null || mAccessToken.equals("")){
            throw new AccesTokenNullException(AccesTokenNullException.DEFAULT_MSG);
        }
    }

}
