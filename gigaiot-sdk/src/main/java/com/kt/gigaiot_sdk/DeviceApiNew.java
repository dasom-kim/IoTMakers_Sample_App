package com.kt.gigaiot_sdk;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.DeviceApiResponse;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.DeviceStatus;
import com.kt.gigaiot_sdk.data.DeviceStatusResponse;
import com.kt.gigaiot_sdk.error.AccesTokenNullException;
import com.kt.gigaiot_sdk.error.ReqParamException;
import com.kt.gigaiot_sdk.network.APIClient;
import com.kt.gigaiot_sdk.network.APIList;
import com.kt.gigaiot_sdk.network.ApiConstants;
import com.kt.gigaiot_sdk.network.data.SvrResponse;
import com.kt.gigaiot_sdk.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ceoko on 15. 3. 23..
 */
public class DeviceApiNew {

    private final String TAG = DeviceApiNew.class.getSimpleName();

    APIList apiList;
    private APICallback<DeviceApiResponse> apiCallback;

    private String mAccessToken;

    public DeviceApiNew(String accessToken, APICallback<DeviceApiResponse> apiCallback) {
        this.mAccessToken = "Bearer " + accessToken;
        this.apiCallback = apiCallback;
    }

    public void deviceRegistration(String svcTgtSeq, String devNm, String spotDevId, String devPwd,
                                                 String devModelNm, String termlMakrNm, String protId, String bindTypeCd, String mbrSeq,
                                                String mbrId, String rootGwCnctId, String type) {
        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
        if(!Utils.isValidParams(svcTgtSeq, devNm, spotDevId, devPwd, devModelNm, termlMakrNm, protId,
                                bindTypeCd, mbrSeq, mbrId, rootGwCnctId, type)){
            //throw new ReqParamException(ReqParamException.DEFAULT_MSG);
            apiCallback.onFail();
            return;
        }

        JsonObject requestObj = new JsonObject();

        try {
            requestObj.addProperty("svcTgtSeq", svcTgtSeq);
            requestObj.addProperty("spotDevId", spotDevId);
            requestObj.addProperty("devNm", devNm);
            requestObj.addProperty("devPwd", devPwd);
            requestObj.addProperty("devModelNm", devModelNm);
            requestObj.addProperty("termlMakrNm", termlMakrNm);
            requestObj.addProperty("protId", protId);
            requestObj.addProperty("bindTypeCd", bindTypeCd);
            requestObj.addProperty("mbrSeq", mbrSeq);
            requestObj.addProperty("mbrId", mbrId);
            requestObj.addProperty("rootGwCnctId", rootGwCnctId);
            requestObj.addProperty("type", type);

            Log.d(TAG, "deviceRegistration requestJson = " + requestObj);
        } catch (Exception e){
            e.printStackTrace();
        }

        // 콜백 호출
        apiCallback.onStart();

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Device>> call = apiList.doPostDeviceRegistration(svcTgtSeq, ApiConstants.MIME_PREFIX + ApiConstants.MIME_JSON, mAccessToken, requestObj);
        call.enqueue(new Callback<SvrResponse<Device>>() {
            @Override
            public void onResponse(Call<SvrResponse<Device>> call, Response<SvrResponse<Device>> response) {
                SvrResponse<Device> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    ArrayList<Device> arrayResult = new ArrayList<>();
                    arrayResult.add(result.getData());
                    apiCallback.onDoing(new DeviceApiResponse(result.getResponseCode(), result.getMessage(), arrayResult));
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

    public void uploadDeviceImg(DeviceNew device, String filePath){

        //error handling - user access token missing
        checkAccessToken();

        //error handling - parameter missing
//        if(!Utils.isValidParams(device.getSvcTgtSeq(), device.getSpotDevSeq(), filePath)){
        if(!Utils.isValidParams(device.getTarget().getSequence(), device.getSequence(), filePath)){
            apiCallback.onFail();
            return;
        }

        // 콜백 호출
        apiCallback.onStart();

        File uploadFile = new File(filePath);
        MultipartBody.Part body = null;

        if(!uploadFile.exists()){
            apiCallback.onDoing(new DeviceApiResponse(ApiConstants.CODE_NG, "file is not exist!! path = " + filePath, null));
            return;
        } else {
            Log.d(TAG, "file is exist!! path = " + filePath + "," + mAccessToken);

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);
            body = MultipartBody.Part.createFormData("atcFile", filePath.substring(filePath.lastIndexOf("/") + 1), requestBody);
        }

        apiList = APIClient.getAUTHClient().create(APIList.class);
        Call<SvrResponse<Device>> call;

//        if(TextUtils.isEmpty(device.getAtcFileSeq())) { //최초 등록시
//            call = apiList.doPostDeviceUploadImage(device.getSvcTgtSeq(),
//                    device.getSpotDevSeq(),
//                    mAccessToken,
//                    body);
//        } else { //기 등록된 이미지 수정시
//            call = apiList.doPostDeviceUpdateImage(device.getSvcTgtSeq(),
//                    device.getSpotDevSeq(),
//                    mAccessToken,
//                    body,
//                    device.getAtcFileSeq());
//        }

        if(TextUtils.isEmpty(device.getImageFileId())) { //최초 등록시
            call = apiList.doPostDeviceUploadImage(device.getTarget().getSequence(),
                    device.getSequence(),
                    mAccessToken,
                    body);
        } else { //기 등록된 이미지 수정시
            call = apiList.doPostDeviceUpdateImage(device.getTarget().getSequence(),
                    device.getSequence(),
                    mAccessToken,
                    body,
                    device.getAuthenticationCertificate().getSequence());
        }

        call.enqueue(new Callback<SvrResponse<Device>>() {
            @Override
            public void onResponse(Call<SvrResponse<Device>> call, Response<SvrResponse<Device>> response) {
                SvrResponse<Device> result = response.body();

                if (result != null && result.getResponseCode().equals(ApiConstants.CODE_OK)) {
                    apiCallback.onDoing(new DeviceApiResponse(result.getResponseCode(), result.getMessage(), null));
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
