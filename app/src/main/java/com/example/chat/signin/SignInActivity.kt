package com.example.chat.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.chat.R
import com.example.chat.databinding.ActivitySignInBinding
import com.example.chat.main.MainActivity

class SignInActivity : AppCompatActivity() ,SignInNavigator{
    lateinit var SignInViewBinding:ActivitySignInBinding
    lateinit var signinViewModel:SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_sign_in)
        SignInViewBinding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(SignInViewBinding.root)

        signinViewModel=ViewModelProvider(this).get(SignInViewModel::class.java)
        SignInViewBinding.vm=signinViewModel
        signinViewModel.navigator=this
        val ttb= AnimationUtils.loadAnimation(this@SignInActivity,R.anim.ttb)
        val stb=AnimationUtils.loadAnimation(this,R.anim.stb)
        val btt = AnimationUtils.loadAnimation(this,R.anim.btt)


        SignInViewBinding.imgAnim.startAnimation(stb)
        SignInViewBinding.appbar.startAnimation(btt)
    }

    override fun showLogs(msg: String) {
        Log.e("Success",msg)
    }

    override fun goToMain() {
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    override fun btnClicked(msg: String) {
        Log.e("flag","something went wrong ${msg}")
        Toast.makeText(this,"Btn is Clicked! ${msg}",Toast.LENGTH_LONG).show()
    }
}