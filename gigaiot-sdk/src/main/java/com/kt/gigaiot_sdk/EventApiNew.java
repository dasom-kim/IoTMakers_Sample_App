package com.kt.gigaiot_sdk;

import android.util.Log;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.Event;
import com.kt.gigaiot_sdk.data.EventApiResponse;
import com.kt.gigaiot_sdk.data.EventLog;
import com.kt.gigaiot_sdk.error.AccesTokenNullException;
import com.kt.gigaiot_sdk.error.ReqParamException;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.network.data.SvrResponse;
import com.kt.gigaiot_sdk.util.Utils;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ceoko on 15. 3. 23..
 * Updated by DASOM
 * 이벤트 목록, 이벤트 로그 목록(두가지 버전) API
 */
public class EventApiNew {

    private final String TAG = EventApiNew.class.getSimpleName();

    APIList apiList;
    private APICallback<EventApiResponse> apiCallback;

    private String mAccessToken;

    public EventApiNew(String accessToken, APICallback<EventApiResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    public void getEventList(String mbrId, String svcTgt){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(mbrId, svcTgt)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<Event>>> call = apiList.doGetEventList(1, 10, svcTgt, mbrId, mAccessToken);
        call.enqueue(new Callback<SvrResponse<Data<Event>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<Event>>> call, Response<SvrResponse<Data<Event>>> response) {
                SvrResponse<Data<Event>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new EventApiResponse(result.getResponseCode(), result.getMessage(), result.getData().getRows()));
                } else {
                    //apiCallback.onDoing(new EventApiResponse(result.getResponseCode(), result.getMessage(), null));
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<Event>>> call, Throwable t) {
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

    public void getEventLogList(String spotDevSeq, String svcTgtSeq, String eventId, long startTime){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(spotDevSeq, svcTgtSeq, eventId, startTime)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        Log.d(TAG, "Result: " + spotDevSeq + "," + svcTgtSeq + "," + eventId + "," + startTime);

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<EventLog>>> call = apiList.doGetEventLogList(spotDevSeq, svcTgtSeq, eventId, startTime, mAccessToken);
        call.enqueue(new Callback<SvrResponse<Data<EventLog>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<EventLog>>> call, Response<SvrResponse<Data<EventLog>>> response) {
                SvrResponse<Data<EventLog>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new EventApiResponse(result.getResponseCode(), result.getMessage(), null, result.getData().getRows()));
                } else {
                    //apiCallback.onDoing(new EventApiResponse(result.getResponseCode(), result.getMessage(), null));
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<EventLog>>> call, Throwable t) {
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

    public void getEventLogList(String spotDevSeq, String svcTgtSeq, String eventId, long startTime, long endTime){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(spotDevSeq, svcTgtSeq, eventId, startTime, endTime)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<EventLog>>> call = apiList.doGetEventLogListToStart(spotDevSeq, svcTgtSeq, eventId, startTime, endTime, mAccessToken);

        call.enqueue(new Callback<SvrResponse<Data<EventLog>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<EventLog>>> call, Response<SvrResponse<Data<EventLog>>> response) {
                SvrResponse<Data<EventLog>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new EventApiResponse(result.getResponseCode(), result.getMessage(), null, result.getData().getRows()));
                } else {
                    //apiCallback.onDoing(new EventApiResponse(resource.getResponseCode(), resource.getMessage(), null));
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<EventLog>>> call, Throwable t) {
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
