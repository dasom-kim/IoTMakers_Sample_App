package com.kt.gigaiot_sdk;

import android.util.Log;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.BindType;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.Protocol;
import com.kt.gigaiot_sdk.data.ProtocolApiResponse;
import com.kt.gigaiot_sdk.data.RootGw;
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
 * 프로토콜 목록, Bind Type 목록 API
 */
public class ProtocolApiNew {

    private final String TAG = ProtocolApiNew.class.getSimpleName();

    APIList apiList;
    private APICallback<ProtocolApiResponse> apiCallback;

    private String mAccessToken;

    public ProtocolApiNew(String accessToken, APICallback<ProtocolApiResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    /**
     * 프로토콜 리스트 가져오기
     */
    public void getProtocolList(){

        //error handling - user access token missing
        checkAccessToken();

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<Protocol>>> call = apiList.doGetProtocols(mAccessToken);
        call.enqueue(new Callback<SvrResponse<Data<Protocol>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<Protocol>>> call, Response<SvrResponse<Data<Protocol>>> response) {
                SvrResponse<Data<Protocol>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new ProtocolApiResponse(result.getResponseCode(), result.getMessage(), result.getData().getRows()));
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<Protocol>>> call, Throwable t) {
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


    /**
     * BindType 리스트 가져오기
     */
    public void getBindTypeList(String protId){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(protId)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        Log.d(TAG, "protId: "+ protId);

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<BindType>>> call = apiList.doGetBindtypes(protId, mAccessToken);
        call.enqueue(new Callback<SvrResponse<Data<BindType>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<BindType>>> call, Response<SvrResponse<Data<BindType>>> response) {
                SvrResponse<Data<BindType>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new ProtocolApiResponse(result.getResponseCode(), result.getMessage(), null, result.getData().getRows()));
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<BindType>>> call, Throwable t) {
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

    /**
     * 상위 게이트웨이 연결 아이디 가져오기
     */
    public void getRootGwCncId(String svcTgtSeq, String protId, String bindTypeCd){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(svcTgtSeq, protId, bindTypeCd)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        Log.d(TAG, "svcTgtSeq: " + svcTgtSeq +", protId: "+ protId +", bindTypeCd:"+ bindTypeCd);

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<RootGw>>> call = apiList.doGetRootgwcncid(svcTgtSeq, protId, bindTypeCd, mAccessToken);
        call.enqueue(new Callback<SvrResponse<Data<RootGw>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<RootGw>>> call, Response<SvrResponse<Data<RootGw>>> response) {
                SvrResponse<Data<RootGw>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new ProtocolApiResponse(result.getResponseCode(), result.getMessage(), null, null, result.getData().getRows()));
                } else {
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<RootGw>>> call, Throwable t) {
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
