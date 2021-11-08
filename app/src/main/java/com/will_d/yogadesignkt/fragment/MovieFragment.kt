package com.will_d.yogadesignkt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.RetrofitService
import com.will_d.yogadesignkt.item.MovieItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

public class MovieFragment : Fragment() {

    val items : ArrayList<MovieItem> = arrayListOf<MovieItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        naverOpenApiMovieLoadData()
//        boxOfficeOpenApiLoadData()

    }
    val naverOpenApiMovieLoadData : ()->Unit = {
        val clientId = "4xYhrcGFaH6EpRGIGPJf"
        val clientSecret = "0rS3Id2ZMc"

        val baseUrl :String ="https://openapi.naver.com/"

        val builder:Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl(baseUrl)
        builder.addConverterFactory(ScalarsConverterFactory.create())
        val retrofit:Retrofit = builder.build()
        val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)
        val call : Call<String> = retrofitService.naverOpenApiMovieLoadData("베놈2", clientId, clientSecret)

        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("naver", response.body() as String)

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("naver", t.message as String)

            }

        })


    }

    val boxOfficeOpenApiLoadData : ()->Unit = {
        val baseUrl = "http://www.kobis.or.kr/"
        val builder:Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl(baseUrl)
        builder.addConverterFactory(ScalarsConverterFactory.create())
        val retrofit:Retrofit = builder.build()
        val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)
        val call : Call<String> = retrofitService.boxOfficeOpenApiLoadData("2f54970e58874ec5d2efc1ff35228264", "20211105")
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("boxOfficeOpenApiLoadData", response.body() as String)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })


    }
}