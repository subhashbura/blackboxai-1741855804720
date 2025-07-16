package com.provizit.qrscanner.Services;

import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface API {

    @GET("masters/getuserDetails")
    Call<Model> getuserDetails(@Header("Authorization") String Bearer,@Header("DeviceId") String header, @Query("id") String id);

     @POST("setup/securityLogin")
    Call<Model> getuserLogin(@Header("Authorization") String Bearer,@Header("DeviceId") String header,@Body JsonObject jsonBody);

//    setup/getqrcodeStatus
//            l_id = "login location id"
//    type = "meeting / checkin / material / workpermit
//    mid =  id
//            idval = email / mobile

    @GET("setup/getqrcodeStatus")
    Call<Model1> getqrcodeStatus(@Header("Authorization") String Bearer,@Header("DeviceId") String header,@Query("comp_id") String comp_id,@Query("l_id") String l_id, @Query("type") String type, @Query("mid") String mid, @Query("idval") String  idval);

 }
