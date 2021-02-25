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

    //메인액티비티 통신 담당
    @GET("/main")
    Call<ServiceInterface>getMain(@Query("id") String id);

    //전공과목 검색
    @GET("/search_major")
    Call<ResponseBody>SearchMajor(@QueryMap Map<String, String> option); //key와 value

    //교양과목 검색
    @GET("/search_Electives")
    Call<ResponseBody>SearchElectives(@QueryMap Map<String,String> option);

    //장바구니에 과목 담기
    @GET("/reserve")
    Call<ResponseBody>getReserve(@QueryMap Map<String, String> option);

    //장바구니 과목 삭제
    @GET("/reserve_delete")
    Call<ResponseBody>ReserveDelete(@QueryMap Map<String, String> option);

    //수강신청 과목 삭제
    @GET("/register_delete")
    Call<ResponseBody>RegisterDelete(@QueryMap Map<String, String> option);

    //장바구니 내역 불러오기
    @GET("/getReserve_list")
    Call<ResponseBody>getReserve_list(@QueryMap Map<String, String> option);

    //장바구니에 담은 과목을 수강신청할 때 사용1
    @GET("/getReserve_register")
    Call<ResponseBody>getReserve_register(@QueryMap Map<String, String> option);

    //장바구니에 담은 과목을 수강신청할 때 사용2
    @GET("/register_reserve")
    Call<ResponseBody>register_reserve(@QueryMap Map<String, String> option);

    //사용자가 직접 검색 후 수강신청
    @GET("/register_search")
    Call<ResponseBody>register_search(@QueryMap Map<String, String> option);

    //학수번호와 분반을 직접 입력하여 수강신청 시 사용
    @GET("/register_classnum")
    Call<ResponseBody>register_classnum(@QueryMap Map<String, String> option);

    //수강신청 과목 리스트 불러오기
    @GET("/getRegister_list")
    Call<ResponseBody>getRegister_list(@QueryMap Map<String, String> option);

    //로그인 통신
    @FormUrlEncoded
    @POST("/login")
    Call<ServiceInterface> postData(@FieldMap HashMap<String, Object> params);

}



