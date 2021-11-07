package com.will_d.yogadesignkt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.item.VideoItem

class VideoAdapter(val context:Context, val videoItems : ArrayList<VideoItem>) : Adapter<VideoAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle : TextView by lazy { itemView.findViewById(R.id.tv_title) }
        val tvSubTitle : TextView by lazy { itemView.findViewById(R.id.tv_subTitle) }
        val tvDesc : TextView by lazy { itemView.findViewById(R.id.tv_dec) }

        val pv : PlayerView by lazy { itemView.findViewById(R.id.exo_player) }
        val player : SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()

        init {
            pv.player = player
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView : View = LayoutInflater.from(context).inflate(R.layout.recycler_vedio, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item : VideoItem = videoItems.get(position)
        holder.tvTitle.text = item.title
        holder.tvSubTitle.text = item.subtitle
        holder.tvDesc.text = item.description

        val mediaItem:MediaItem = MediaItem.fromUri(item.sources as String)
        holder.player.setMediaItem(mediaItem)
        holder.player.prepare()


    }

    override fun getItemCount(): Int {
        return videoItems.size
    }

}