package com.example.chat.addroom

import android.net.Uri
import androidx.lifecycle.MutableLiveData

object imsprovider {
    var newImg:Boolean?=false
    var selectedimg:Uri?=null
    var f=MutableLiveData<Boolean>()
    var deletePhoto=MutableLiveData<Boolean>()


    var immg=MutableLiveData<Uri>()
}