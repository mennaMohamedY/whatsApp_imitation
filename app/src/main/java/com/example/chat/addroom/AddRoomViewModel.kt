package com.example.chat.addroom

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddRoomViewModel : ViewModel() {
    val img=ObservableField<Drawable>()
    val imgg=MutableLiveData<Uri>()

    val profilepic=ObservableField<Uri>()

    val username=ObservableField<String>()
    val status=ObservableField<String>()
    val phonenum=ObservableField<String>()



}