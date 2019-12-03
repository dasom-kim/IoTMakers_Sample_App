package com.kt.gigaiot_sdk;

import android.util.Log;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.Member;
import com.kt.gigaiot_sdk.data.MemberApiResponse;
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
 */
public class MemberApiNew {

    private final String TAG = MemberApiNew.class.getSimpleName();

    APIList apiList;
    private APICallback<MemberApiResponse> apiCallback;

    private String mAccessToken;

    public MemberApiNew(String accessToken, APICallback<MemberApiResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    /**
     * Member Info 가져오기
     */
    public void getMemberInfo(final String mbrSeq){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(mbrSeq)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Data<Member>>> call = apiList.doGetMemberInfo(mbrSeq, 1, 10, mAccessToken);
        call.enqueue(new Callback<SvrResponse<Data<Member>>>() {
            @Override
            public void onResponse(Call<SvrResponse<Data<Member>>> call, Response<SvrResponse<Data<Member>>> response) {
                SvrResponse<Data<Member>> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new MemberApiResponse(result.getResponseCode(), result.getMessage(), result.getData().getRows().get(0)));
                } else {
                    //apiCallback.onDoing(new MemberApiResponse(result.getResponseCode(), result.getMessage(), null));
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SvrResponse<Data<Member>>> call, Throwable t) {
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
