package com.will_d.yogadesignkt

import android.telecom.Call
import com.google.android.exoplayer2.text.span.TextAnnotation
import okhttp3.MultipartBody
import retrofit2.http.*
import java.sql.Struct

public interface RetrofitService {

    //여기는 비디오 부분임
    @GET("yogaDesignKt/video/videoData.json")
    fun getVideoData() : retrofit2.Call<String>


    //여기는 멤버들 체크하는 부분임
    //아이디 체크하는 부분
    @GET("yogaDesignKt/member/memberCheckedLoadDB.php")
    fun memberCheckedLoadDB() : retrofit2.Call<String>


    //처음에는 아이디랑 비밀번호를 저장하고 나머지 닉네임이나 프로필 유알엘은 업데이트 형식으로 할거야!!!    //그냥 구분임자로 쓸려고 하는데 가능한지 모르겠당???
    @Multipart
    @POST("yogaDesignKt/member/memberDataSendDB.php")
    fun memberDataSendDB(@PartMap dataPart:Map<String, String>, @Part filePart : MultipartBody.Part?) : retrofit2.Call<String>


    @GET("v1/search/movie.json")
    fun naverOpenApiMovieLoadData(@Query("query") query :String, @Header("X-Naver-Client-Id") id:String, @Header("X-Naver-Client-Secret") secret : String) : retrofit2.Call<String>

    @GET("kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
    fun boxOfficeOpenApiLoadData(@Query("key") key :String, @Query("targetDt") targetDt :String) : retrofit2.Call<String>




}