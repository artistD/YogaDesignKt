package com.will_d.yogadesignkt.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.item.MessageItem
import de.hdodenhof.circleimageview.CircleImageView

class ChattingAdapter(var context :Context, var items : ArrayList<MessageItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        var itemView : View

        val item : MessageItem = items.get(position)
        val pref : SharedPreferences = context.getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
        val myId = pref.getString("id", "")
        if (item.id.equals(myId)){
            itemView =inflater.inflate(R.layout.listview_my_messagebox, parent, false)
        }else{
            itemView =inflater.inflate(R.layout.listview_other_messagebox, parent, false)
        }

        val civ = itemView.findViewById<CircleImageView>(R.id.civ)
        val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        val tvMsg = itemView.findViewById<TextView>(R.id.tv_msg)
        val tvTime = itemView.findViewById<TextView>(R.id.tv_time)

        Glide.with(context).load(item.imgUrl).into(civ)
        tvName.text = item.nickName
        tvMsg.text = item.msg
        tvTime.text = item.time
        return itemView
    }
}