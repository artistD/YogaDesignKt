package com.will_d.yogadesignkt.fragment

import android.icu.number.IntegerWidth
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.RetrofitService
import com.will_d.yogadesignkt.item.MovieItem
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

public class MovieFragment : Fragment() {

    val items : ArrayList<MovieItem> = arrayListOf<MovieItem>()
    var num =0

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
        boxOfficeOpenApiLoadData()

    }
    val naverOpenApiMovieLoadData : (String, Map<String, String>)->Unit = {
            title:String, map:Map<String, String>
        ->

        val clientId = "4xYhrcGFaH6EpRGIGPJf"
        val clientSecret = "0rS3Id2ZMc"

        val baseUrl :String ="https://openapi.naver.com/"

        val builder:Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl(baseUrl)
        builder.addConverterFactory(ScalarsConverterFactory.create())
        val retrofit:Retrofit = builder.build()
        val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)
        val call : Call<String> = retrofitService.naverOpenApiMovieLoadData(title, 1, clientId, clientSecret)

        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("naver", response.body() as String)
                val jsonObject : JSONObject = JSONObject(response.body())
                val a = jsonObject.getJSONArray("items")

                    val c = a.getJSONObject(0)
                    val image = c.getString("image")
                    val subtitle = c.getString("subtitle")
                    val director = c.getString("director")
                    val actor = c.getString("actor")
                    val userRating = c.getString("userRating")
                    items.add(MovieItem(map.get("rank") as String, title, map.get("openMovie") as String, map.get("audiSum") as String, image, subtitle, director, actor, userRating))


                val movieItem = items.get(num)
                num++
                Log.i("dwqfqw", movieItem.imgUrl + " : " + movieItem.rank)

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
        val call : Call<String> = retrofitService.boxOfficeOpenApiLoadData("2f54970e58874ec5d2efc1ff35228264", "20211108")
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("boxOfficeOpenApiLoadData", response.body() as String)
                val jsonObject : JSONObject = JSONObject(response.body())
                val a :JSONObject = jsonObject.getJSONObject("boxOfficeResult")
                val b :JSONArray = a.getJSONArray("dailyBoxOfficeList")
                for (i in 0 until b.length()){
                    val c :JSONObject = b.getJSONObject(i)
                    val rank = c.getString("rank")
                    val title = c.getString("movieNm")
                    val openMovie = c.getString("openDt")
                    val audiSum = c.getString("audiAcc")

                    val map = mutableMapOf<String, String>()
                    map.put("rank", rank)
                    map.put("openMovie", openMovie)
                    map.put("audiSum", audiSum)

                    naverOpenApiMovieLoadData(title, map)
                    //todo : 야기서는 도대체 어떻게 되는거지???
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })


    }
}