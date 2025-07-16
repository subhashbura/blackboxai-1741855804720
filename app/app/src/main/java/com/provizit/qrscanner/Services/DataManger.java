package com.provizit.qrscanner.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class DataManger {






    public static final String ROOT_URL = "https://liveapi.provizit.com/provizit/resources/";
//    public static final String ROOT_URL = "http://18.221.211.226:8080/provizit/resources/";
// public static final String ROOT_URL = "http://10.183.249.27:8080/roshan/resources/";
    public static final String IMAGE_URL = "https://provizit.com";
//  public static final String IMAGE_URL = "http://10.183.249.27";
    private static DataManger dataManager;
    private Retrofit retrofit,retrofit1,retrofit2;


    private DataManger() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        HttpLoggingInterceptor logging1 = new HttpLoggingInterceptor();
        logging1.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient1 = new OkHttpClient.Builder();
        httpClient1.connectTimeout(30, TimeUnit.SECONDS);
        httpClient1.readTimeout(120, TimeUnit.SECONDS);
        httpClient1.writeTimeout(120, TimeUnit.SECONDS);
        httpClient1.addInterceptor(logging1);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder().baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        retrofit1 = new Retrofit.Builder().baseUrl(IMAGE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        retrofit2 = new Retrofit.Builder().baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient1.build())
                .build();

    }
    public static DataManger getDataManager() {
        if (dataManager == null) {
            dataManager = new DataManger();
        }
        return dataManager;
    }
    public void getuserDetails(Callback<Model> cb, Context context, String id) {
        API apiService = retrofit.create(API.class);
        String newEncrypt = encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.getuserDetails(bearer,newEncrypt,id);
        call.enqueue((Callback<Model>) cb);
    }
    public void userLogin(Callback<Model> cb,Context context, JsonObject data) {
        API apiService = retrofit.create(API.class);
        String newEncrypt = encrypt(context,true);
        String bearer = "Bearer" + newEncrypt;
        Call<Model> call = apiService.getuserLogin(bearer,newEncrypt,data);
        call.enqueue((Callback<Model>) cb);
    }
    public void getqrcodeStatus(Callback<Model1> cb,Context context,String l_id,String type,String m_id,String idval) {
        API apiService = retrofit.create(API.class);
        String newEncrypt = encrypt(context,false);
        String bearer = "Bearer" + newEncrypt;

        SharedPreferences sharedPreferences1 = context.getSharedPreferences("Provizit_Scanner", MODE_PRIVATE);

        Call<Model1> call = apiService.getqrcodeStatus(bearer,newEncrypt,sharedPreferences1.getString("comp_id", null),l_id,type,m_id,idval);
        call.enqueue((Callback<Model1>) cb);
    }

    public static String encrypt(Context context,Boolean isAdmin) {
        AESUtil aesUtil = new AESUtil(context);
        Calendar calendar = Calendar.getInstance();
        if (isAdmin) {
            return aesUtil.encrypt("admin_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60),"egems_2013_grms_2017_provizit_2020");
        } else {
            if(context != null) {
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("Provizit_Scanner", MODE_PRIVATE);
                String value = sharedPreferences1.getString("comp_id", null) + "_" + ((calendar.getTimeInMillis() / 1000) - timezone() - 60);
                String aesEncypit =  aesUtil.encrypt(value,"egems_2013_grms_2017_provizit_2020");
                System.out.println("Subahsh " + aesUtil.decrypt("egems_2013_grms_2017_provizit_2020",aesEncypit));
                return aesEncypit;
            }
            return "";
        }

    }
    public void decript(String val){

    }

    public static int timezone(){
        Date d = new Date();
        int timeZone = d.getTimezoneOffset()*60;
        return timeZone;
    }

}
