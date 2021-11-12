package com.will_d.yogadesignkt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.utils.widget.MockView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.item.MovieItem
import org.w3c.dom.Text

class MovieAdapter(val context:Context, val items:ArrayList<MovieItem>) : RecyclerView.Adapter<MovieAdapter.VH>(){

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv : ImageView by lazy { itemView.findViewById(R.id.iv) }
        val tvRank : TextView by lazy { itemView.findViewById(R.id.tv_rank) }
        val tvTitle : TextView by lazy { itemView.findViewById(R.id.tv_title) }
        val tvOpenMovie : TextView by lazy { itemView.findViewById(R.id.tv_openMovie) }

        init {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_movie, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val movieItem = items.get(position)
        Glide.with(context).load(movieItem.imgUrl).into(holder.iv)
        holder.tvRank.text = movieItem.rank
        holder.tvTitle.text = movieItem.title
        holder.tvOpenMovie.text = movieItem.openMovie
    }

    override fun getItemCount(): Int {
        return items.size
    }
}