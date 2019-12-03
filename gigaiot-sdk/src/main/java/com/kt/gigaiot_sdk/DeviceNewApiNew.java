package com.kt.gigaiot_sdk;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.DeviceApiResponseNew;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.Paging;
import com.kt.gigaiot_sdk.error.AccesTokenNullException;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.network.data.SvrResponseNew;
import com.kt.gigaiot_sdk.util.Utils;

import java.io.StringReader;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ceoko on 15. 3. 23..
 */
public class DeviceNewApiNew {

    private final String TAG = DeviceNewApiNew.class.getSimpleName();

    APIList apiList;
    private APICallback<DeviceApiResponseNew> apiCallback;

    private String mAccessToken;

    public DeviceNewApiNew(String accessToken, APICallback<DeviceApiResponseNew> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    /**
     * Device 리스트 가져오기
     * @param
     */
    public void getNewDeviceList(int offset) {

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(offset)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponseNew<DeviceNew>> call = apiList.doGetNewDeviceList(mAccessToken, offset, 10);
        call.enqueue(new Callback<SvrResponseNew<DeviceNew>>() {
            @Override
            public void onResponse(Call<SvrResponseNew<DeviceNew>> call, Response<SvrResponseNew<DeviceNew>> response) {
                SvrResponseNew<DeviceNew> result = response.body();

                // OK 메세지 없는 API
                if (response.isSuccessful()) {
                    apiCallback.onDoing(new DeviceApiResponseNew(result.getResponseCode(), result.getMessage(),
                            result.getPaging(), result.getData()));
                // Unauthorized
                } else if (response.code() == 401) {
                    apiCallback.onDoing(new DeviceApiResponseNew(null, null,
                            null, null));
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponseNew<DeviceNew>> call, Throwable t) {
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

    public void putNewdeviceModify(String sequence, String name, String used,
                                   String published, String status, String authenticationKey,
                                   String connectionId, String connectionType, String modifier){

        //error handling - user access token missing
        checkAccessToken();

        // 콜백 호출
        apiCallback.onStart();

        if(!Utils.isValidParams(sequence, name, used, published, status, authenticationKey, connectionId, connectionType, modifier)){
            apiCallback.onFail();
            return;
        }

        JsonObject requestObj = new JsonObject();

        try {
            requestObj.addProperty("name", name);
            requestObj.addProperty("description", "");
            requestObj.addProperty("used", used);
            requestObj.addProperty("published", published);
            requestObj.addProperty("status", status);
            requestObj.addProperty("firmwareVersion", "");
            requestObj.addProperty("authenticationId", "");
            requestObj.addProperty("authenticationKey", authenticationKey);
            requestObj.addProperty("connectionId", connectionId);
            requestObj.addProperty("connectionType", connectionType);
            requestObj.addProperty("modifier", modifier);

        } catch (Exception e){
            e.printStackTrace();
        }

        apiList = APIClient.getAUTHClient().create(APIList.class);

        Call<SvrResponseNew> call = apiList.doPostNewDeviceModify(
                ApiConstants.MIME_PREFIX + ApiConstants.MIME_JSON,
                mAccessToken,
                sequence,
                requestObj);

        call.enqueue(new Callback<SvrResponseNew>() {
            @Override
            public void onResponse(Call<SvrResponseNew> call, Response<SvrResponseNew> response) {
                SvrResponseNew result = response.body();

                // OK 메세지 없는 API
                if (response.isSuccessful()) {
                    apiCallback.onDoing(new DeviceApiResponseNew(result.getResponseCode(), result.getMessage(), result.getPaging(), result.getData()));
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponseNew> call, Throwable t) {
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

    public void putNewdeviceCtrl(String sequence, String code, String value){
        //error handling - user access token missing
        checkAccessToken();

        // 콜백 호출
        apiCallback.onStart();

        if(!Utils.isValidParams(sequence, code)){
            apiCallback.onFail();
            return;
        }

        JsonObject requestObj = new JsonObject();

        try {
            JsonArray array = new JsonArray();

            JsonObject requestSubObj = new JsonObject();
            requestSubObj.addProperty("code", code);
            requestSubObj.addProperty("value", value);

            array.add(requestSubObj);

            requestObj.add("sensingTags", array);
        } catch (Exception e){
            e.printStackTrace();
        }

        apiList = APIClient.getAUTHClient().create(APIList.class);

        Call<SvrResponseNew> call = apiList.doPutNewDeviceCtrl(
                ApiConstants.MIME_PREFIX + ApiConstants.MIME_JSON,
                mAccessToken,
                sequence,
                "true",
                requestObj);

        call.enqueue(new Callback<SvrResponseNew>() {
            @Override
            public void onResponse(Call<SvrResponseNew> call, Response<SvrResponseNew> response) {
                SvrResponseNew result = response.body();

                // OK 메세지 없는 API
                if (response.isSuccessful()) {
                    apiCallback.onDoing(new DeviceApiResponseNew(result.getResponseCode(), result.getMessage(), result.getPaging(), result.getData()));
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponseNew> call, Throwable t) {
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
