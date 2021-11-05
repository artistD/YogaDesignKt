package com.will_d.yogadesignkt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

public class VedioFragment : Fragment() {

    val videoItems : ArrayList<VideoItem> = arrayListOf<VideoItem>();
//    val videoRecycler: RecyclerView by lazy { view.?findViewById(R.id.recycler_vedio) }
    lateinit var videoRecycler: RecyclerView
    lateinit var adapter: VideoAdapter

//    lateinit var youtubeFragment : YouTubePlayerFragment
//    lateinit var youtubeFragment2 : YouTubePlayerFragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vedio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        videoRecycler = view.findViewById(R.id.recycler_vedio)
        adapter = VideoAdapter(activity as Context, videoItems)
        videoRecycler.adapter = adapter



    }

    override fun onResume() {
        super.onResume()
//        youtubePlay()
          RetrofitHellper.getVideoData(videoItems, adapter)
    }

    //todo : 교수님께 질문 : 화면, 기능, 데이터를 나눈다는것이 도대체 무엇일까??? 나는 일단 레트로핏에 관련된 부분들을 레트로핏 헬퍼에 몰아넣기로 했다 근데
    //밑에있는것은 그저 기능인데 어떻게 분류를 하는것이 좋을까???


//    val youtubePlay : () ->Unit = {
//        val fragmentManager : FragmentManager = (activity as MainActivity).supportFragmentManager
//        youtubeFragment = (fragmentManager.findFragmentById(R.id.fragment_youtube)) as YouTubePlayerFragment
//        youtubeFragment2 = (fragmentManager.findFragmentById(R.id.fragment_youtube2)) as YouTubePlayerFragment
//
//
//        youtubeFragment.initialize("first", object : YouTubePlayer.OnInitializedListener{
//            override fun onInitializationSuccess(
//                p0: YouTubePlayer.Provider?,
//                p1: YouTubePlayer?,
//                p2: Boolean
//            ) {
//                p1?.cueVideo("jJp7s8yN1ww")
//            }
//
//            override fun onInitializationFailure(
//                p0: YouTubePlayer.Provider?,
//                p1: YouTubeInitializationResult?
//            ) {
//            }
//
//        })
//
//        youtubeFragment2.initialize("second", object : YouTubePlayer.OnInitializedListener{
//            override fun onInitializationSuccess(
//                p0: YouTubePlayer.Provider?,
//                p1: YouTubePlayer?,
//                p2: Boolean
//            ) {
//                p1?.cueVideo("P8_8C2kA1WY")
//            }
//
//            override fun onInitializationFailure(
//                p0: YouTubePlayer.Provider?,
//                p1: YouTubeInitializationResult?
//            ) {
//
//            }
//
//        })
//    }

}