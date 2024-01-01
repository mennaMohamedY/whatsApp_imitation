package com.example.chat.login

import androidx.core.os.persistableBundleOf
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat.addroom.checkPhoneNumber
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginViewModel: ViewModel() {
    var email = ObservableField<String?>()
    var emailError = ObservableField<String?>()

    var password = ObservableField<String?>()
    var passwordError = ObservableField<String?>()

    var firstName = ObservableField<String?>()
    var firstNameError = ObservableField<String?>()

    var lastName = ObservableField<String?>()
    var lastNameError = ObservableField<String?>()

    var navigator:LoginNavigator?=null
    val auth = FirebaseAuth.getInstance()

    val phoneNumber=ObservableField<String>()
    val phoneNumberError= ObservableField<String>()


    fun login(){
        if (validate()){
            //navigator?.showLogs("successful login")

                auth.createUserWithEmailAndPassword(email.get().toString(),password.get().toString()).addOnCompleteListener(
                    object :OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {
                            if(p0.isSuccessful){
                                navigator?.showError("Success!")
                                insertUserToDB(p0.result.user!!.uid)
                                navigator?.goToMain()
                            }else{
                                navigator?.showError("unfortunatly something went wrong " + "${p0.exception!!.localizedMessage}")
                            }
                        }

                    })

        }
    }
    fun AlreadyRegistered(){
        navigator?.navigateToSignIn()
    }

    fun validate():Boolean{
        var valid1=true
        var valid2=true
        var valid3=true
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

        if (!CheckPhoneNum(phoneNumber.get().toString())){
            valid3=false
            phoneNumberError.set("Invalid phone number!!")
        }else{
            valid3=true
            phoneNumberError.set(null)

        }

        valid= valid1 && valid2 && valid3

        return valid
    }
    fun insertUserToDB(uid:String){
        val usr=User(
            userID = uid,
            userEmail = email.get().toString(),
            userPassword = password.get().toString(),
            userName = "${firstName.get() }  ${lastName.get()}",
            userimgUri = "https://firebasestorage.googleapis.com/v0/b/chatapp-58700.appspot.com/o/images%2Fperson.jpg?alt=media&token=85c5e9d8-94db-48f0-8838-938d0aacc3b4"

        )
        UserProvider.user=usr
        FireStoreUtiles().InsertUserToDB(usr).addOnCompleteListener {
            if(it.isSuccessful){
                navigator?.showLogs("Success from FireStorUtiles side")
            }else{
                navigator?.showLogs("Failed from FireStoreUtiles side")
            }
        }
    }


}