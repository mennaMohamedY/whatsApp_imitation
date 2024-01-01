package com.example.chat.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.chat.R
import com.example.chat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mainDataBinding:ActivityMainBinding
    lateinit var mainViewModel:MainViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        mainDataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainDataBinding.root)

        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)
//        mainDataBinding.myRooms.setOnClickListener({
//            mainDataBinding.leftBorder.setBackgroundColor(getColor(R.color.white))
//            pushFragment(MyRoomsFragment())
//        })
//
//        mainDataBinding.browse.setOnClickListener({
//            mainDataBinding.rightBorder.setBackgroundColor(getColor(R.color.white))
//            pushFragment(BrowseRoomFragment())
//        })
//
//        mainDataBinding.addbtn.setOnClickListener({
//            val intent =Intent(this,AddRoomActivity::class.java)
//            startActivity(intent)
//        })
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController=navHostFragment.navController
        val navView=mainDataBinding.botomNavigationView
        navView.setupWithNavController(navController)




    }


//    fun pushFragment(fragment: Fragment){
//        supportFragmentManager.beginTransaction().replace(mainDataBinding.frameLayout.id,fragment).commit()
//    }
}