package com.example.chat.signin

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.chat.login.LoginNavigator
import com.example.chat.login.checkEmailAddress
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel:ViewModel() {
    var email = ObservableField<String?>()
    var emailError = ObservableField<String?>()

    var password = ObservableField<String?>()
    var passwordError = ObservableField<String?>()


    var navigator: SignInNavigator?=null
    val auth = FirebaseAuth.getInstance()


    fun login(){
        //navigator?.goToMain()

        if (validate()){
            navigator?.showLogs("successful login")

            auth.signInWithEmailAndPassword(email.get()!!,password.get()!!).addOnCompleteListener{
                if (it.isSuccessful){
                    val usr=User(
                        userID = it.result.user?.uid,
                        userEmail = it.result.user?.email,
                        userPassword = password.get(),
                        //userimgUri = it.result.user?.photoUrl.toString(),

                    )
                    UserProvider.user=usr
                    navigator?.goToMain()

                }
            }
        }
    }



    fun validate():Boolean{
        var valid1=true
        var valid2=true
        var valid =true
        if (email.get().isNullOrEmpty()){
            emailError.set("Please Insert your Email!")
            valid1 =false
        }else if(checkEmailAddress(email.get().toString()) == true){
            emailError.set(null)
            valid1=true

        }else if(checkEmailAddress(email.get().toString()) == false){
            emailError.set("Please Enter a valid Email!")
            valid1 =false
        }
        else{
            emailError.set(null)
            valid1=true
        }

        if(password.get().isNullOrEmpty()){
            passwordError.set("password is required!")
            valid2=false
        }else if(password.get().toString().length <=5 ){
            passwordError.set("Password must be longer than 5 chars!")
            valid2=false
        }
        else{
            passwordError.set(null)
            valid2=true

        }
        valid= valid1 && valid2
        navigator?.showLogs("valid Login")

        return valid
    }



}