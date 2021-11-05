package com.will_d.yogadesignkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MemberJoinActivity : AppCompatActivity() {


    val idItems : ArrayList<String> = arrayListOf<String>()

    val etId :EditText by lazy { findViewById(R.id.et_id) }
    val tvIdCheckedFalse : TextView by lazy { findViewById(R.id.tv_id_checked_false) }
    val tvIdCheckedTrue :TextView by lazy { findViewById(R.id.tv_id_checked_true) }

    val etPassword : EditText by lazy { findViewById(R.id.et_password) }
    val etPasswordCheck : EditText by lazy { findViewById(R.id.et_password_check) }

    var isIdChecked :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_join)
        memberCheckedLoadDB()
    }

    fun clickMemberJoin(view: View) {
        var isPasswordEqual = etPassword.text.toString().equals(etPasswordCheck.text.toString())
        if (isPasswordEqual && isIdChecked){
            memberDataSendDB(etId.text.toString(), etPassword.text.toString())
        }else{
            Toast.makeText(this, "회원정보가 올바르지 않습니다.", Toast.LENGTH_LONG).show()
        }
    }

    fun clickIdChecked(view: View) {
        //여기서도 DB작업이 필요해야
        for (i in 0 until idItems.size){
            if (etId.text.toString().equals(idItems.get(i))){
                tvIdCheckedTrue.visibility = View.GONE
                tvIdCheckedFalse.visibility = View.GONE

                tvIdCheckedTrue.visibility = View.VISIBLE
                isIdChecked = true
                break

            }else{
                tvIdCheckedTrue.visibility = View.GONE
                tvIdCheckedFalse.visibility = View.GONE

                tvIdCheckedFalse.visibility = View.VISIBLE
                isIdChecked = false
            }
        }
    }

    val memberCheckedLoadDB : ()->Unit ={
        val retrofit : Retrofit = RetrofitHellper.getRetrofritScalars()
        val retrofitService:RetrofitService = retrofit.create(RetrofitService::class.java)
        val call : Call<String> = retrofitService.memberCheckedLoadDB()
        idItems.clear()
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("memberCheckedLoadDB", response.body() as String)
                val jsonStr : String? = response.body()
                val jsonArray:JSONArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()){
                    val jsonObject : JSONObject = jsonArray.getJSONObject(i)
                    val id :String = jsonObject.getString("id")
                    idItems.add(id)
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("memberCheckedLoadDB", t.message as String)

            }

        })

    }

    val memberDataSendDB : (String, String)->Unit = {
            id:String, pw:String
        ->
        val retrofit : Retrofit = RetrofitHellper.getRetrofritScalars()
        val retrofitService:RetrofitService = retrofit.create(RetrofitService::class.java)

        val filePart : MultipartBody.Part? = null
        val dataPart = mutableMapOf<String, String>()

        //todo:이거질문해야함
//        val datapart2 : Map<String, String> = mutableMapOf<String, String>()
//        datapart2.put

        dataPart.put("id", id)
        dataPart.put("pw", pw)

        val call : Call<String> = retrofitService.memberDataSendDB("INSERT", dataPart, filePart)
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("memberDataSendDB", response.body() as String)
                Toast.makeText(this@MemberJoinActivity, "회원가입 완료", Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("memberDataSendDB", t.message as String)
            }

        })

    }


}