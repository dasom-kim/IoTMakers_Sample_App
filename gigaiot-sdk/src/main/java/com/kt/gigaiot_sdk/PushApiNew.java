package com.kt.gigaiot_sdk;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.PushApiResponse;
import com.kt.gigaiot_sdk.data.PushTypePair;
import com.kt.gigaiot_sdk.error.AccesTokenNullException;
import com.kt.gigaiot_sdk.error.ReqParamException;
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

/**
 * Created by ceoko on 15. 6. 18..
 */
public class PushApiNew {

    private final String TAG = PushApiNew.class.getSimpleName();

    public static final String PUSH_TYPE_GCM        = "01";

    public static final String PUSH_MSG_TYPE_COLLECT    = "01";
    public static final String PUSH_MSG_TYPE_COMPLEX    = "02";
    public static final String PUSH_MSG_TYPE_OUTBREAK   = "03";
    public static final String PUSH_MSG_TYPE_ECSND      = "04";

    APIList apiList;
    private APICallback<PushApiResponse> apiCallback;

    private String mAccessToken;

    public PushApiNew(String accessToken, APICallback<PushApiResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    public void gcmSessionRegistration(String mbrSeq, String applId,
                                                  String applUuidVal, ArrayList<PushTypePair> pushTypePairs){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(mbrSeq, applId, applUuidVal) || pushTypePairs == null || pushTypePairs.size() == 0){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            Log.d(TAG, "please input parameters");
            apiCallback.onFail();
            return;
        }

        Log.d(TAG, "mbrSeq: " + mbrSeq + ", applId: " + applId + ", applUuidVal: " + applUuidVal + "," + pushTypePairs.toString());

        JsonObject requestJson = new JsonObject();

        try {
            requestJson.addProperty("pushTypeCd", PUSH_TYPE_GCM);
            requestJson.addProperty("mbrSeq", mbrSeq);
            requestJson.addProperty("cretrId", mbrSeq);
            requestJson.addProperty("amdrId", mbrSeq);
            requestJson.addProperty("applId", applId);
            requestJson.addProperty("applUuidVal", applUuidVal);
            requestJson.addProperty("cretrId", mbrSeq);
            requestJson.addProperty("amdrId", mbrSeq);

            JsonArray array = new JsonArray();

            for(PushTypePair pair : pushTypePairs) {
                JsonObject subscriptionObj = new JsonObject();
                subscriptionObj.addProperty("svcTgtSeq", pair.getSvcTgtSeq());
                subscriptionObj.addProperty("msgTypeCd", pair.getMsgTypeCd());

                array.add(subscriptionObj);
            }

            requestJson.add("subscriptions", array);

            android.util.Log.d(TAG, "gcmSessionRegistration requestJson = " + requestJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Device>> call = apiList.doPostPushSessionReg(ApiConstants.MIME_PREFIX + ApiConstants.MIME_JSON, mAccessToken, requestJson);
        call.enqueue(new Callback<SvrResponse<Device>>() {
            @Override
            public void onResponse(Call<SvrResponse<Device>> call, Response<SvrResponse<Device>> response) {
                SvrResponse<Device> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    ArrayList<Device> arrayResult = new ArrayList<>();
                    arrayResult.add(result.getData());
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Device>> call, Throwable t) {
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

    public void gcmSessionDelete(String applUuidVal){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(applUuidVal)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            Log.d(TAG, "please input parameters");
            apiCallback.onFail();
            return;
        }

        Log.d(TAG, "applUuidVal: " + applUuidVal);

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Device>> call = apiList.doPostPushSessionDel(applUuidVal, ApiConstants.MIME_PREFIX + ApiConstants.MIME_JSON, mAccessToken);
        call.enqueue(new Callback<SvrResponse<Device>>() {
            @Override
            public void onResponse(Call<SvrResponse<Device>> call, Response<SvrResponse<Device>> response) {
                SvrResponse<Device> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    ArrayList<Device> arrayResult = new ArrayList<>();
                    arrayResult.add(result.getData());
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Device>> call, Throwable t) {
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
