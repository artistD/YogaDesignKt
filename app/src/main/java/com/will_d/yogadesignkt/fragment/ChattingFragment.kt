package com.will_d.yogadesignkt.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.will_d.yogadesignkt.Global
import com.will_d.yogadesignkt.adapter.ChattingAdapter
import com.will_d.yogadesignkt.R
import com.will_d.yogadesignkt.item.MessageItem
import java.util.*
import kotlin.collections.ArrayList

public class ChattingFragment : Fragment() {
    val items : ArrayList<MessageItem> = arrayListOf<MessageItem>()
    lateinit var listviewChat : ListView
    lateinit var chattingAdapter : ChattingAdapter

    lateinit var etMsg:EditText
    lateinit var ivSend:ImageView

    lateinit var firebaseDatabase : FirebaseDatabase
    lateinit var chatRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etMsg = view.findViewById(R.id.et_msg)
        ivSend = view.findViewById(R.id.iv_send)

        listviewChat = view.findViewById(R.id.listview_chat)
        chattingAdapter = ChattingAdapter(activity as Context, items)
        listviewChat.adapter = chattingAdapter

        ivSend.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val pref = context?.getSharedPreferences("Data", MODE_PRIVATE)
                val id = pref?.getString("id","")

                val nickname : String = Global.nickName
                val message : String = etMsg.text.toString()
                val profileUrl = Global.profileUrl

                val calendar = Calendar.getInstance()
                val time = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

                val item = MessageItem(id, profileUrl, nickname, message, time)
                chatRef.push().setValue(item)

                etMsg.setText("")

                val imm : InputMethodManager = (context?.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken,0)


            }

        })


    }

    val firebaseChat : () -> Unit = {
        firebaseDatabase = FirebaseDatabase.getInstance()
        chatRef = firebaseDatabase.getReference("chat")

        chatRef.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(MessageItem::class.java)
                items.add(item as MessageItem)
                chattingAdapter.notifyDataSetChanged()
                listviewChat.setSelection(items.size-1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}