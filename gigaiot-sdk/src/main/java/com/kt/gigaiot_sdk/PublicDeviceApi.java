package com.kt.gigaiot_sdk;

import android.util.Log;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.DeviceOpen;
import com.kt.gigaiot_sdk.data.DeviceOpenResponse;
import com.kt.gigaiot_sdk.error.AccesTokenNullException;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.network.data.SvrResponse;
import com.kt.gigaiot_sdk.util.Utils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Updated by DASOM
 * 공공 디바이스 목록 API
 */
public class PublicDeviceApi {
    private final String TAG = DeviceApiStatus.class.getSimpleName();

    APIList apiList;
    private APICallback<DeviceOpenResponse> apiCallback;

    private String mAccessToken;

    public PublicDeviceApi(String accessToken, APICallback<DeviceOpenResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    public void doGetPublicDeviceList(int pageNum, ArrayList<String> ctgryCd) {

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(ctgryCd)){
            apiCallback.onFail();
            return;
        }

        String ctgryList = "";

        for (int i = 0; i < ctgryCd.size(); i++) {
            if (i == 0) {
                ctgryList = ctgryCd.get(i);
            } else {
                ctgryList = ctgryList + "," + ctgryCd.get(i);
            }
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<DeviceOpen>>> call =  apiList.doGetPublicDeviceList(pageNum, 10, ctgryList, mAccessToken);
        call.enqueue(new Callback<SvrResponse<Data<DeviceOpen>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<DeviceOpen>>> call, Response<SvrResponse<Data<DeviceOpen>>> response) {
                SvrResponse<Data<DeviceOpen>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new DeviceOpenResponse(result.getResponseCode(), result.getMessage(),
                            result.getData().getTotal(), result.getData().getPage(), result.getData().getRowNum(), result.getData().getRows()));
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<DeviceOpen>>> call, Throwable t) {
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

    private void checkAccessToken(){
        if(mAccessToken == null || mAccessToken.equals("")){
            throw new AccesTokenNullException(AccesTokenNullException.DEFAULT_MSG);
        }
    }
}
