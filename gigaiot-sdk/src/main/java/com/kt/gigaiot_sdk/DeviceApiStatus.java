package com.kt.gigaiot_sdk;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.Callback.ImageCallback;
import com.kt.gigaiot_sdk.data.DeviceStatus;
import com.kt.gigaiot_sdk.data.DeviceStatusResponse;
import com.kt.gigaiot_sdk.data.Paging;
import com.kt.gigaiot_sdk.error.AccesTokenNullException;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.network.data.SvrResponse;
import com.kt.gigaiot_sdk.network.data.SvrResponseNew;
import com.kt.gigaiot_sdk.util.Utils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ceoko on 15. 3. 23..
 */
public class DeviceApiStatus {

    private final String TAG = DeviceApiStatus.class.getSimpleName();

    APIList apiList;
    private APICallback<DeviceStatusResponse> apiCallback;

    private String mAccessToken;

    public DeviceApiStatus(String accessToken, APICallback<DeviceStatusResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    public void doPostDeviceStatus(String inclGwCntId, String inclSpotDevId) {

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(inclGwCntId,inclSpotDevId)){
            apiCallback.onFail();
            return;
        }

        JsonObject requestObj = new JsonObject();

        try {
            requestObj.addProperty("inclGwCntId", inclGwCntId);

            JsonArray array = new JsonArray();

            array.add(inclSpotDevId);

//            for (int i = 0; i < inclSpotDevId.size(); i++) {
//                array.add(inclSpotDevId.get(i));
//            }

            requestObj.add("inclSpotDevId", array);

            Log.d(TAG, "deviceStatus requestJson = " + requestObj);
        } catch (Exception e){
            e.printStackTrace();
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponseNew<DeviceStatus>> call = apiList.doPostDeviceStatus(mAccessToken, requestObj);

        call.enqueue(new Callback<SvrResponseNew<DeviceStatus>>() {
            @Override
            public void onResponse(Call<SvrResponseNew<DeviceStatus>> call, Response<SvrResponseNew<DeviceStatus>> response) {
                SvrResponseNew<DeviceStatus> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new DeviceStatusResponse(result.getResponseCode(), result.getMessage(), result.getData()));
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponseNew<DeviceStatus>> call, Throwable t) {
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
