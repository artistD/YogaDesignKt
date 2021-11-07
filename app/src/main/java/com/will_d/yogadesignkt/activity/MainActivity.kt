package com.will_d.yogadesignkt.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.will_d.yogadesignkt.*
import com.will_d.yogadesignkt.fragment.ChattingFragment
import com.will_d.yogadesignkt.fragment.HomeFragment
import com.will_d.yogadesignkt.fragment.MovieFragment
import com.will_d.yogadesignkt.fragment.VedioFragment

class MainActivity : AppCompatActivity() {

    val fragments: Array<Fragment> = arrayOf<Fragment>(HomeFragment(), MovieFragment(), VedioFragment(), ChattingFragment())
    val bnv:BottomNavigationView by lazy { findViewById(R.id.bnv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val fragmentManager  = supportFragmentManager
        val tran:FragmentTransaction = fragmentManager.beginTransaction()

        tran.hide(fragments[1])
        tran.hide(fragments[2])
        tran.hide(fragments[3])

        tran.add(R.id.container, fragments[0])
        tran.add(R.id.container, fragments[1])
        tran.add(R.id.container, fragments[2])
        tran.add(R.id.container, fragments[3])
        tran.commit()

        val bnv:BottomNavigationView = findViewById(R.id.bnv)
        bnv.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val tran:FragmentTransaction = fragmentManager.beginTransaction()


                //엘비스 연산자를 사용하기는 하는데 도대체 뭐 어떻게 해야하는지를 모르겠다
                //만약 arrayListOf<>()를 사용한다면 null체크를 못하니까 다른 알고리즘을 떠올려서 코딩을 해야하는것인가???
                tran.hide(fragments[0])
                tran.hide(fragments[1])
                tran.hide(fragments[2])
                tran.hide(fragments[3])


                val h:Int = item.itemId
                when(h){
                    R.id.bnv_home ->{
                        tran.show(fragments[0])
                    }

                    R.id.bnv_movie ->{
                        tran.show(fragments[1])
                    }

                    R.id.bnv_videos ->{
                        tran.show(fragments[2])
                    }

                    R.id.bnv_chatting ->{
                        tran.show(fragments[3])
                    }
                }
                tran.commit()
                return true
            }

        })




    }
}
