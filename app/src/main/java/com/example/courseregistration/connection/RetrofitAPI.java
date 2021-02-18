package com.example.courseregistration.connection;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitAPI {
    @GET("/connect")
    Call<ServiceInterface>getConnect(@Query("id") String id);

    @GET("/main")
    Call<ServiceInterface>getMain(@Query("id") String id);

    @GET("/search_major")
    Call<ResponseBody>SearchMajor(@QueryMap Map<String, String> option); //key와 value

    @GET("/search_Electives")
    Call<ResponseBody>SearchElectives(@QueryMap Map<String,String> option);

    @GET("/reserve")
    Call<ResponseBody>getReserve(@QueryMap Map<String, String> option);

    @GET("/reserve_delete")
    Call<ResponseBody>ReserveDelete(@QueryMap Map<String, String> option);

    @GET("/register_delete")
    Call<ResponseBody>RegisterDelete(@QueryMap Map<String, String> option);

    @GET("/getReserve_list")
    Call<ResponseBody>getReserve_list(@QueryMap Map<String, String> option);

    @GET("/getReserve_register")
    Call<ResponseBody>getReserve_register(@QueryMap Map<String, String> option);

    @GET("/register_reserve")
    Call<ResponseBody>register_reserve(@QueryMap Map<String, String> option);

    @GET("/register_search")
    Call<ResponseBody>register_search(@QueryMap Map<String, String> option);

    @GET("/register_classnum")
    Call<ResponseBody>register_classnum(@QueryMap Map<String, String> option);

    @GET("/getRegister_list")
    Call<ResponseBody>getRegister_list(@QueryMap Map<String, String> option);

    @FormUrlEncoded
    @POST("/login")
    Call<ServiceInterface> postData(@FieldMap HashMap<String, Object> params);

}



