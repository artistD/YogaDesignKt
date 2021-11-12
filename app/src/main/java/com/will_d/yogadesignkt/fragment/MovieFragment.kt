package com.will_d.yogadesignkt.fragment

import android.content.Context
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.JsonObject
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.RetrofitService
import com.will_d.yogadesignkt.activity.MainActivity
import com.will_d.yogadesignkt.adapter.MovieAdapter
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
    lateinit var recyclerview :RecyclerView
    lateinit var adapter : MovieAdapter

    lateinit var mainActivty : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivty = activity as MainActivity


        var imgUrl : String = "https://cdn.pixabay.com/photo/2016/11/22/06/32/girl-1848477_1280.jpg"

//        items.add(MovieItem("1", "dqwqwd", "qwdqw", imgUrl))

        recyclerview = view.findViewById(R.id.recycler_movie)
        adapter = MovieAdapter(activity as Context, items)
        val gridLayoutManager : GridLayoutManager = GridLayoutManager(activity, 2)
        recyclerview.layoutManager = gridLayoutManager
        recyclerview.adapter = adapter


        boxOfficeOpenApiLoadData()

    }

    val naverOpenApiMovieLoadData : (String, MovieItem)->Unit = {
            title:String, movieItem:MovieItem
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
                mainActivty.pg.visibility = View.INVISIBLE
                mainActivty.blur.visibility = View.INVISIBLE
                Log.i("naver", response.body() as String)
                val jsonObject : JSONObject = JSONObject(response.body())
                val a = jsonObject.getJSONArray("items")

                    val c = a.getJSONObject(0)
                    val image = c.getString("image")

                    movieItem.imgUrl = image
                    adapter.notifyItemInserted(movieItem.rank.toInt()-1)

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("naver", t.message as String)

            }

        })


    }

    val boxOfficeOpenApiLoadData : ()->Unit = {
        mainActivty.pg.visibility = View.VISIBLE
        mainActivty.blur.visibility = View.VISIBLE
        val baseUrl = "http://www.kobis.or.kr/"
        val builder:Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl(baseUrl)
        builder.addConverterFactory(ScalarsConverterFactory.create())
        val retrofit:Retrofit = builder.build()
        val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)
        val call : Call<String> = retrofitService.boxOfficeOpenApiLoadData("2f54970e58874ec5d2efc1ff35228264", "20211109")

        items.clear()
        adapter.notifyDataSetChanged()
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
                    items.add(MovieItem(rank, title, openMovie, ""))

                    naverOpenApiMovieLoadData(title, items.get(i))
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })


    }
}