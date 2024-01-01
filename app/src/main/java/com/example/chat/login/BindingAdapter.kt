package com.example.chat.login

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("Error")
fun ErroInTextInputLayout(layout:TextInputLayout,msgEror:String?){
    layout.error=msgEror

}