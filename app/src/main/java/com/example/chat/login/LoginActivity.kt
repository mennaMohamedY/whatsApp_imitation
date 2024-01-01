package com.example.chat.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chat.R
import com.example.chat.databinding.ActivityLoginBinding
import com.example.chat.main.MainActivity
import com.example.chat.signin.SignInActivity

class LoginActivity : AppCompatActivity() ,LoginNavigator{


    lateinit var dataBinding:ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    var progressDialog:ProgressDialog?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        dataBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        loginViewModel= ViewModelProvider(this).get(LoginViewModel::class.java)
        dataBinding.vm=loginViewModel
        loginViewModel.navigator=this



    }

    override fun showLogs(msg: String) {
        Log.e("success!","success!, ${msg}")
    }

    override fun goToMain() {
        val intent=Intent(this,MainActivity::class.java)
        progressDialog!!.dismiss()
        startActivity(intent)

    }

    override fun showError(msg: String) {
        progressDialog=ProgressDialog(this)
        progressDialog!!.setMessage(msg)
        progressDialog!!.show()
    }

    override fun navigateToSignIn() {
        val intent=Intent(this,SignInActivity::class.java)
        startActivity(intent)
    }
}