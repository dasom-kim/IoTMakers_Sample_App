package com.kt.gigaiot_sdk.network;

/**
 * Created by dasom-kim on 2018.10.05
 */

import com.google.gson.JsonObject;
import com.kt.gigaiot_sdk.data.AccessToken;
import com.kt.gigaiot_sdk.data.BindType;
import com.kt.gigaiot_sdk.data.Data;
import com.kt.gigaiot_sdk.data.Device;
import com.kt.gigaiot_sdk.data.DeviceNew;
import com.kt.gigaiot_sdk.data.DeviceOpen;
import com.kt.gigaiot_sdk.data.DeviceStatus;
import com.kt.gigaiot_sdk.data.Event;
import com.kt.gigaiot_sdk.data.EventLog;
import com.kt.gigaiot_sdk.data.Log;
import com.kt.gigaiot_sdk.data.Member;
import com.kt.gigaiot_sdk.data.Protocol;
import com.kt.gigaiot_sdk.data.RootGw;
import com.kt.gigaiot_sdk.data.SvcTgt;
import com.kt.gigaiot_sdk.data.SvcTgtNew;
import com.kt.gigaiot_sdk.data.TagStrm;
import com.kt.gigaiot_sdk.network.data.SvrResponse;
import com.kt.gigaiot_sdk.network.data.SvrResponseNew;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIList {
    //Open API 변경
    String OPEN_API_HOST_ADDRESS = "api/v1";  //API-GW host
    String OPEN_API_HOST_ADDRESS_UP_VERSION = "api/v1.1";  //API-GW host
    String OPEN_API_HOST_MASTER_ADDRESS = "masterapi/v1";  //API-GW host
    String OPEN_API_CORE_HOST = "coreapi/v1";   //API-GW event host
    String OPEN_API_PUSH_HOST = "pushapi/v1";  //API-GW push host

    // OAuth API
    @FormUrlEncoded
    @POST("oauth/token")
    Call<AccessToken> doPostOauthToken(@Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                       @Field(ApiConstants.OAUTH_GRANT_TYPE_KEY) String grantType,
                                       @Field("username") String id,
                                       @Field("password") String password);

    // Device API
    @POST(OPEN_API_HOST_ADDRESS+"/devices/status")
    Call<SvrResponseNew<DeviceStatus>> doPostDeviceStatus(@Header(ApiConstants.OAUTH_AUTHORIZATION) String authoricationString,
                                                          @Body JsonObject requestJson);

    @FormUrlEncoded
    @POST(OPEN_API_HOST_ADDRESS+"/devices/{svcTgtSeq}")
    Call<SvrResponse<Device>> doPostDeviceRegistration(@Path("svcTgtSeq") String svcTgtSeq,
                                                       @Header(ApiConstants.CONTENT_TYPE) String contentType,
                                                       @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                       @Body JsonObject requestJson);

    @PUT(OPEN_API_HOST_ADDRESS_UP_VERSION+"/devices/{sequence}")
    Call<SvrResponseNew> doPostNewDeviceModify(@Header(ApiConstants.CONTENT_TYPE) String contentType,
                                               @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                               @Path("sequence") String sequence,
                                               @Body JsonObject requestJson);

    @Multipart
    @POST(OPEN_API_HOST_MASTER_ADDRESS+"/devices/{svcTgtSeq}/{spotDevSeq}/image")
    Call<SvrResponse<Device>> doPostDeviceUploadImage(@Path("svcTgtSeq") String svcTgtSeq,
                                                      @Path("spotDevSeq") String spotDevSeq,
                                                      @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                      @Part MultipartBody.Part image);

    @Multipart
    @POST(OPEN_API_HOST_MASTER_ADDRESS+"/devices/{svcTgtSeq}/{spotDevSeq}/imageUpdate")
    Call<SvrResponse<Device>> doPostDeviceUpdateImage(@Path("svcTgtSeq") String svcTgtSeq,
                                                      @Path("spotDevSeq") String spotDevSeq,
                                                      @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                      @Part MultipartBody.Part image,
                                                      @Query("atcFileSeq") String atcFileSeq);

    // Get Device Image
    @GET(OPEN_API_HOST_MASTER_ADDRESS+"/devices/{svcTgtSeq}/{spotDevSeq}/imageBase64")
    Call<SvrResponse> getImageBase64(@Path("svcTgtSeq") String svcTgtSeq,
                                     @Path("spotDevSeq") String spotDevSeq,
                                     @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    @GET(OPEN_API_HOST_ADDRESS_UP_VERSION+"/devices")
    Call<SvrResponseNew<DeviceNew>> doGetNewDeviceList(@Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                                @Query("offset") int offset,
                                                                @Query("limit") int limit);

    @PUT(OPEN_API_HOST_ADDRESS_UP_VERSION+"/devices/{sequence}/sensingTags")
    Call<SvrResponseNew> doPutNewDeviceCtrl(@Header(ApiConstants.CONTENT_TYPE) String contentType,
                                            @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                            @Path("sequence") String sequence,
                                            @Query("sync") String sync,
                                            @Body JsonObject requestJson);

    // Protocol API
    @GET(OPEN_API_HOST_ADDRESS+"/protocols/types")
    Call<SvrResponse<Data<Protocol>>> doGetProtocols(@Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    @GET(OPEN_API_HOST_ADDRESS+"/protocols/{protId}/bindtypes")
    Call<SvrResponse<Data<BindType>>> doGetBindtypes(@Path("protId") String protId,
                                                     @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    @GET(OPEN_API_HOST_ADDRESS+"/protocols/{svcTgtSeq}/{protId}/{bindTypeCd}")
    Call<SvrResponse<Data<RootGw>>> doGetRootgwcncid(@Path("svcTgtSeq") String svcTgtSeq,
                                                     @Path("protId") String protId,
                                                     @Path("bindTypeCd") String bindTypeCd,
                                                     @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    // ServiceTarget API
    @GET(OPEN_API_HOST_ADDRESS_UP_VERSION+"/targets")
    Call<SvrResponseNew<SvcTgtNew>> doGetNewSvctgtSeqList(@Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                       @Query("offset") int offset,
                                                       @Query("limit") int limit);

    // Tag Stream API
    @GET(OPEN_API_HOST_ADDRESS+"/streams/{spotDevId}")
    Call<SvrResponse<Data<TagStrm>>> doGetTagstreamList(@Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                        @Path("spotDevId") String spotDevId,
                                                        @Query("pageNum") int pageNum,
                                                        @Query("pageCon") int pageCon);

    @GET(OPEN_API_HOST_ADDRESS+"/streams/{spotDevId}/log")
    Call<SvrResponse<ArrayList<Log>>> doGetTagstreamLog(@Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                        @Path("spotDevId") String spotDevId,
                                                        @Query("period") String wantPeriod,
                                                        @Query("count") String Count);

    @GET(OPEN_API_HOST_ADDRESS+"/streams/{svcTgtSeq}/{spotDevSeq}/log/last")
    Call<SvrResponse<ArrayList<Log>>> doGetTagstreamLogLast(@Path("svcTgtSeq") String svcTgtSeq,
                                                            @Path("spotDevSeq") String spotDevSeq,
                                                            @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    @POST(OPEN_API_HOST_ADDRESS+"/devices/{svcTgtSeq}/{spotDevSeq}/control/async")
    Call<SvrResponse<Void>> doPostTagstreamCtrl(@Path("svcTgtSeq") String svcTgtSeq,
                                                @Path("spotDevSeq") String spotDevSeq,
                                                @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                @Body JsonObject requestJson);

    // Event API
    @GET(OPEN_API_CORE_HOST+"/event/eventList")
    Call<SvrResponse<Data<Event>>> doGetEventList(@Query("pageNum") int pageNum,
                                                  @Query("pageCon") int pageCon,
                                                  @Query("svcTgtSeq") String svcTgtSeq,
                                                  @Query("mbrId") String mbrId,
                                                  @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    @GET(OPEN_API_CORE_HOST+"/event/logLastAccess/{spotDevSeq}/{svcTgtSeq}/{eventId}/{startTime}")
    Call<SvrResponse<Data<EventLog>>> doGetEventLogList(@Path("spotDevSeq") String spotDevSeq,
                                                        @Path("svcTgtSeq") String svcTgtSeq,
                                                        @Path("eventId") String eventId,
                                                        @Path("startTime") long startTime,
                                                        @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    @GET(OPEN_API_CORE_HOST+"/event/logLastAccess/{spotDevSeq}/{svcTgtSeq}/{eventId}/{startTime}/{endTime}")
    Call<SvrResponse<Data<EventLog>>> doGetEventLogListToStart(@Path("spotDevSeq") String spotDevSeq,
                                                               @Path("svcTgtSeq") String svcTgtSeq,
                                                               @Path("eventId") String eventId,
                                                               @Path("startTime") long startTime,
                                                               @Path("endTime") long endTime,
                                                               @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    // Member API
    @GET(OPEN_API_HOST_MASTER_ADDRESS+"/mbrs")
    Call<SvrResponse<Data<Member>>> doGetMemberInfo(@Query("mbrSeq") String mbrSeq,
                                                    @Query("pageNum") int pageNum,
                                                    @Query("pageCon") int pageCon,
                                                    @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    // Push API
    @POST(OPEN_API_PUSH_HOST+"/sessions")
    Call<SvrResponse<Device>> doPostPushSessionReg(@Header(ApiConstants.CONTENT_TYPE) String contentType,
                                                   @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString,
                                                   @Body JsonObject requestJson);

    @DELETE(OPEN_API_PUSH_HOST+"/sessions/{applUuidVal}")
    Call<SvrResponse<Device>> doPostPushSessionDel(@Path("applUuidVal") String applUuidVal,
                                                   @Header(ApiConstants.CONTENT_TYPE) String contentType,
                                                   @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

    // Public Device API
    @GET(OPEN_API_HOST_MASTER_ADDRESS+"/devices/open")
    Call<SvrResponse<Data<DeviceOpen>>> doGetPublicDeviceList(@Query("pageNum") int pageNum,
                                                              @Query("pageCon") int pageCon,
                                                              @Query("ctgryCd") String ctgryCd,
                                                              @Header(ApiConstants.OAUTH_AUTHORIZATION) String authorizationString);

}
