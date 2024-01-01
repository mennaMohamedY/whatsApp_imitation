package com.example.chat.home

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.chat.models.User

object ChatsProvider {
    var pp=MutableLiveData<Uri>()
    var usersID:List<String>?=null
    var emptyUsers= mutableListOf<User?>()
    var emptyUsers2= mutableListOf<User?>()
    var emptyU=MutableLiveData<MutableList<User?>?>()


}