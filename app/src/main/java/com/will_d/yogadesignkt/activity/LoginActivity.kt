package com.will_d.yogadesignkt.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.will_d.yogadesignkt.*
import com.will_d.yogadesignkt.item.MemberInformationItem
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {


    val memberItems:ArrayList<MemberInformationItem> = arrayListOf<MemberInformationItem>()
    var isIdChecked : Boolean = false
    var myId: String =""
    var myPassword :String= ""

    val etId :EditText by lazy { findViewById(R.id.et_id) }
    val etPw :EditText by lazy { findViewById(R.id.et_pw) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onResume() {
        super.onResume()

        val pref:SharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)
        val nickName = pref.getString("nickName", "")
        val profileUrl = pref.getString("profileUrl","")
        val isLogin = pref.getBoolean("isLogin", false)
        val isFirstPhotoChecked : Boolean = pref.getBoolean("isFirstPhotoChecked", false)
        if (isLogin){
            if (isFirstPhotoChecked){
                Global.nickName = nickName as String
                Global.profileUrl = profileUrl as String
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, UserSettingActivity::class.java))
                finish()
            }
        }
        memberInformation()

    }

    fun clickMemberJoinPage(view: View) {
        startActivity(Intent(this, MemberJoinActivity::class.java))
    }

    fun clickLogin(view: View) {
        for (i in 0 until memberItems.size){
            if (etId.text.toString().equals(memberItems.get(i).id)){
                myId = memberItems.get(i).id
                myPassword = memberItems.get(i).password
                isIdChecked = true
                break

            }else{
                isIdChecked = false
            }

        }

        if (isIdChecked){
            if (etPw.text.toString().equals(myPassword)){
                val pref : SharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)
                val editor : SharedPreferences.Editor = pref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("id", myId)
                editor.commit()
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                //로그인 성공으로 스타트 액티비티 하면됨
                startActivity(Intent(this, UserSettingActivity::class.java))
                finish()

            }else{
                //비밀번호가 일치하지않습니다 토스트띄우고
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
        }



    }

    val memberInformation : ()->Unit = {
        val retrofit : Retrofit = RetrofitHellper.getRetrofritScalars()
        val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)
        val call : Call<String> = retrofitService.memberCheckedLoadDB()

        memberItems.clear()
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val jsonStr : String? = response.body()
                val jsonArray:JSONArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()){
                    val jsonObject : JSONObject = jsonArray.getJSONObject(i)
                    val id :String = jsonObject.getString("id")
                    val password :String = jsonObject.getString("password")
                    memberItems.add(MemberInformationItem(id, password))
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })
    }

    fun clickGoogleLogin(view: View) {

    }
}