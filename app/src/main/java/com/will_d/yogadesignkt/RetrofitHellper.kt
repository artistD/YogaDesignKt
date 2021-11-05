package com.will_d.yogadesignkt

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

public class RetrofitHellper {

    companion object{
        val baseUrl: String = "http://willd88.dothome.co.kr/"

        fun getRetrofritGson() : Retrofit{
            val builder : Retrofit.Builder = Retrofit.Builder()
            builder.baseUrl(baseUrl)
            builder.addConverterFactory(GsonConverterFactory.create())
            val retrofit:Retrofit = builder.build()
            return retrofit
        }

        fun getRetrofritScalars() : Retrofit{
            val builder : Retrofit.Builder = Retrofit.Builder()
            builder.baseUrl(baseUrl)
            builder.addConverterFactory(ScalarsConverterFactory.create())
            val retrofit:Retrofit = builder.build()
            return retrofit
        }


        //:옆에쓰는 것은 자료형에대해서 쓰는것이고, 그 뒤에 쓰는것은 파라미터를 쓰는것이다
        val getVideoData : (ArrayList<VideoItem>, VideoAdapter)->Unit = {
                videoItems:ArrayList<VideoItem>, adapter:VideoAdapter
            ->
            val retrofit: Retrofit = RetrofitHellper.getRetrofritScalars()
            val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)
            val call : Call<String> = retrofitService.getVideoData()

            videoItems.clear()
            adapter.notifyDataSetChanged()

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val jsonStr:String? = response.body()
                    Log.i("getVideoData", jsonStr as String)

                    val jsonObject : JSONObject = JSONObject(jsonStr)
                    val categories : JSONArray = jsonObject.getJSONArray("categories")
                    val jsonObject2 : JSONObject = categories.getJSONObject(0)
                    val videos : JSONArray = jsonObject2.getJSONArray("videos")

                    for (i in 0 until videos.length()){
                        val videoJsonObject : JSONObject = videos.getJSONObject(i)
                        val title :String? = videoJsonObject.getString("title")
                        val subTitle : String? = videoJsonObject.getString("subtitle")
                        val description : String? = videoJsonObject.getString("description")
                        val thumb :String? = videoJsonObject.getString("thumb")


                        val s : JSONArray = videoJsonObject.getJSONArray("sources")
                        val sources :String? = s.getString(0)




                        videoItems.add(VideoItem(title, subTitle, thumb, sources, description))
                        adapter.notifyItemInserted(videoItems.size-1)



                    }


                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("getVideoData", t.message as String)
                }

            })



        }


    }


}