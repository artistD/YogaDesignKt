package com.will_d.yogadesignkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun clickMemberJoinPage(view: View) {
        startActivity(Intent(this, MemberJoinActivity::class.java))
    }
}