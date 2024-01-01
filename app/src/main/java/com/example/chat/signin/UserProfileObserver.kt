package com.example.chat.signin

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.chat.models.User

object UserProfileObserver {
    var profilePicChanged=MutableLiveData<Boolean>()
    var allUsers:List<User>?=null
    var updatedUser:User?=null
    var imgURI=MutableLiveData<Uri>()
    var botmsheetpp=MutableLiveData<Uri>()

}