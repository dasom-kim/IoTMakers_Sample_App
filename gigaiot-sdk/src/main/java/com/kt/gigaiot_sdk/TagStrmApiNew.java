package com.kt.gigaiot_sdk;

import com.google.gson.JsonObject;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.Log;
import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.gigaiot_sdk.data.TagStrmApiResponse;
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
 * Created by ceoko on 15. 3. 24..
 */
public class TagStrmApiNew {

    private final String TAG = TagStrmApiNew.class.getSimpleName();

    public static final String TAGSTRM_DATA = "0000010";
    public static final String TAGSTRM_CTRL = "0000020";

    APIList apiList;
    private APICallback<TagStrmApiResponse> apiCallback;

    private String mAccessToken;

    public TagStrmApiNew(String accessToken, APICallback<TagStrmApiResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    public void getTagStrmList(String spotDevId){

        //error handling - user access token missing
        checkAccessToken();

        if(!Utils.isValidParams(spotDevId)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<TagStrm>>> call = apiList.doGetTagstreamList(mAccessToken, spotDevId, 1, 10);
        call.enqueue(new Callback<SvrResponse<Data<TagStrm>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<TagStrm>>> call, Response<SvrResponse<Data<TagStrm>>> response) {
                SvrResponse<Data<TagStrm>> result = response.body();


                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new TagStrmApiResponse(result.getResponseCode(), result.getMessage(), result.getData().getRows(), null));
                } else {
                    apiCallback.onDoing(new TagStrmApiResponse(result.getResponseCode(), result.getMessage(), null, null));
                    //apiCallback.onFail();
                    android.util.Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<TagStrm>>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    android.util.Log.i(TAG, "Socket Time out. Please try again.");
                } else {
                    android.util.Log.i(TAG, "API error caused by " + t.getMessage());
                }

                apiCallback.onFail();
                call.cancel();
            }
        });
    }

    public void getTagStrmLog(String spotDevId, String wantPeriod, String Count){

        //error handling - user access token missing
        checkAccessToken();

        if(!Utils.isValidParams(spotDevId, wantPeriod, Count)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<ArrayList<Log>>> call = apiList.doGetTagstreamLog(mAccessToken, spotDevId, wantPeriod, Count);
        call.enqueue(new Callback<SvrResponse<ArrayList<Log>>>() {
            @Override
            public void onResponse(Call<SvrResponse<ArrayList<Log>>> call, Response<SvrResponse<ArrayList<Log>>> response) {
                SvrResponse<ArrayList<Log>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new TagStrmApiResponse(result.getResponseCode(), result.getMessage(), null, result.getData()));
                } else {
                    apiCallback.onFail();
                    android.util.Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<ArrayList<Log>>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    android.util.Log.i(TAG, "Socket Time out. Please try again.");
                } else {
                    android.util.Log.i(TAG, "API error caused by " + t.getMessage());
                }

                apiCallback.onFail();
                call.cancel();
            }
        });
    }

    public void getTagStrmLog(String svcTgtSeq, String spotDevSeq){

        //error handling - user access token missing
        checkAccessToken();

        if(!Utils.isValidParams(spotDevSeq, svcTgtSeq)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<ArrayList<Log>>> call = apiList.doGetTagstreamLogLast(svcTgtSeq, spotDevSeq, mAccessToken);
        call.enqueue(new Callback<SvrResponse<ArrayList<Log>>>() {
            @Override
            public void onResponse(Call<SvrResponse<ArrayList<Log>>> call, Response<SvrResponse<ArrayList<Log>>> response) {
                SvrResponse<ArrayList<Log>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new TagStrmApiResponse(result.getResponseCode(), result.getMessage(), null, result.getData()));
                } else {
                    apiCallback.onFail();
                    android.util.Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<ArrayList<Log>>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    android.util.Log.i(TAG, "Socket Time out. Please try again.");
                } else {
                    android.util.Log.i(TAG, "API error caused by " + t.getMessage());
                }

                apiCallback.onFail();
                call.cancel();
            }
        });
    }


    public void sendCtrlMsg(String svcTgtSeq, String spotDevSeq, String spotDevId,
                                          String gwCnctId, String tagStrmId, String tagStrmValTypeCd,
                                          String mbrId, String ctrlMsg){

        //error handling - user access token missing
        checkAccessToken();

        if(!Utils.isValidParams(svcTgtSeq, spotDevSeq, spotDevId, gwCnctId, tagStrmId, tagStrmValTypeCd, mbrId, ctrlMsg)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        android.util.Log.d(TAG, "send Ctrl!");
        JsonObject requestJson = new JsonObject();

        try {
            requestJson.addProperty("mbrId", mbrId);
            requestJson.addProperty("spotDevId", spotDevId);
            requestJson.addProperty("tagStrmValTypeCd", tagStrmValTypeCd);
            requestJson.addProperty("gwCnctId", gwCnctId);
            requestJson.addProperty("tagStrmId", tagStrmId);
            requestJson.addProperty("ctrlMsg", ctrlMsg);

            android.util.Log.d(TAG, "sendCtrlMsg requestJson = " + requestJson);

        } catch (Exception e) {

        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Void>> call = apiList.doPostTagstreamCtrl(svcTgtSeq, spotDevSeq, mAccessToken, requestJson);
        call.enqueue(new Callback<SvrResponse<Void>>() {
            @Override
            public void onResponse(Call<SvrResponse<Void>> call, Response<SvrResponse<Void>> response) {
                SvrResponse<Void> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new TagStrmApiResponse(result.getResponseCode(), result.getMessage(), null, null));
                } else {
                    apiCallback.onDoing(new TagStrmApiResponse(result.getResponseCode(), result.getMessage(), null, null));
                    //apiCallback.onFail();
                    android.util.Log.i(TAG, "API 확인 요망 : " + response.code() + "," + result.getResponseCode());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Void>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    android.util.Log.i(TAG, "Socket Time out. Please try again.");
                } else {
                    android.util.Log.i(TAG, "API error caused by " + t.getMessage());
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
