package com.example.chat.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denzcoskun.imageslider.models.SlideModel
import com.example.chat.R
import com.example.chat.databinding.ActivitySplashBinding
import com.example.chat.login.LoginActivity
import com.example.chat.signin.SignInActivity

class SplashActivity : AppCompatActivity() {
    lateinit var splashBinding:ActivitySplashBinding
    lateinit var imgs:ArrayList<SlideModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        initSliderData()
        splashBinding.imageSlider.setImageList(imgs)
        splashBinding.register.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        splashBinding.gotoSignin.setOnClickListener({
            val intent=Intent(this,SignInActivity::class.java)
            startActivity(intent)
        })




    }
    fun initSliderData(){
        imgs= arrayListOf()
        imgs.add(SlideModel(R.drawable.c3))
        imgs.add(SlideModel(R.drawable.c4))
        imgs.add(SlideModel(R.drawable.c5))

    }
}
