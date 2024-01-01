package com.example.chat.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

data class ChatDataClass(
    var senderName: String? = null,
    var senderID: String? = null,
    var messageID: String? = null,
    var datetime: Timestamp? = null,
    var messsageContent: String? = null,
    var tm: String ?= null,
    var getter_name:String?=null,
    var sendImg:Boolean=false,
    var imgUri:String?=null,
    var tie:String?=null,

) {
    fun getTimee(): String {
        val currenttime = datetime?.toDate() ?: return ""
        val dateformater = SimpleDateFormat("hh:mm")
        return dateformater.format(currenttime)
    }
}




