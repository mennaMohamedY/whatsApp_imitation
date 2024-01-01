package com.example.chat.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userID: String? = null,
    var userEmail: String? = null,
    var userPassword: String? = null,
    var userName: String? = null,
    var status: String? = "Hey there! i'm currently using whatapp",
    var userProfilePic: Boolean? = false,
    var userimgUri: String? = "https://firebasestorage.googleapis.com/v0/b/chatapp-58700.appspot.com/o/images%2Fperson.jpg?alt=media&token=85c5e9d8-94db-48f0-8838-938d0aacc3b4",
    var userPhoneNum: String? = null,
    var senderName:String?=null,
    var getterName:String?=null,
    var lastMsg:String?=null,

) : Parcelable
