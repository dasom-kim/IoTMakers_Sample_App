package com.kt.gigaiot_sdk;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.AccessToken;
import com.kt.gigaiot_sdk.data.GiGaIotOAuthResponse;
import com.kt.gigaiot_sdk.error.ReqParamException;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.util.Utils;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ceoko on 15. 6. 12..
 * Updated by DASOM
 * 인증(로그인) API
 */
public class GigaIotOAuthNew {

    private final String TAG = GigaIotOAuthNew.class.getSimpleName();

    private String mClientId;
    private String mClientSecret;

    APIList apiList;
    private APICallback<GiGaIotOAuthResponse> apiCallback;

    private static final String OAUTH_GRANT_TYPE_PW         = "password";
    private static final String OAUTH_GRANT_TYPE_CREDENTIAL = "client_credentials";

    public GigaIotOAuthNew(String clientId, String clientSecret, APICallback<GiGaIotOAuthResponse> apiCallback) {
        this.mClientId = clientId;
        this.mClientSecret = clientSecret;
        this.apiCallback = apiCallback;
    }

    public void loginWithPassword(String id, String password){
        String grantType = "";

        //error handling - parameter missing
        if(!Utils.isValidParams(id, password)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        if(TextUtils.isEmpty(id)){
            grantType = OAUTH_GRANT_TYPE_CREDENTIAL;
        } else {
            grantType = OAUTH_GRANT_TYPE_PW;
        }

        String authorizationString = "Basic " + Base64.encodeToString((mClientId + ":" + mClientSecret).getBytes(),Base64.NO_WRAP); //Base64.NO_WRAP flag

        // 로그인 시도 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<AccessToken> call = apiList.doPostOauthToken(authorizationString, grantType, id, password);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken result = response.body();

                if (result != null && TextUtils.isEmpty(result.getError())) {
                    apiCallback.onDoing(new GiGaIotOAuthResponse(ApiConstants.CODE_OK, "", result.getAccess_token(), result.getMbr_seq()));
                } else {
                    //apiCallback.onDoing(new GiGaIotOAuthResponse(ApiConstants.CODE_NG, resource.getError_description(), null, null));
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
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

    public void login() {
        String authorizationString = "Basic " + Base64.encodeToString((mClientId + ":" + mClientSecret).getBytes(),Base64.NO_WRAP); //Base64.NO_WRAP flag

        // 로그인 시도 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<AccessToken> call = apiList.doPostOauthToken(authorizationString, OAUTH_GRANT_TYPE_CREDENTIAL, null, null);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken result = response.body();

                if (result != null && TextUtils.isEmpty(result.getError())) {
                    apiCallback.onDoing(new GiGaIotOAuthResponse(ApiConstants.CODE_OK, "", result.getAccess_token(), result.getMbr_seq()));
                } else {
                    //apiCallback.onDoing(new GiGaIotOAuthResponse(ApiConstants.CODE_NG, resource.getError_description(), null, null));
                    apiCallback.onFail();
                    Log.i(TAG, "API 확인 요망 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                //apiCallback.onDoing(new GiGaIotOAuthResponse(ApiConstants.CODE_NG, null, null, null));
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
