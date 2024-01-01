package com.example.chat.addroom

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("SetABackground")
fun changeImg(img:ShapeableImageView,imgUri:Uri?){
    //img.setImageDrawable(imgres)
    img.setImageURI(imgUri)
    Glide.with(img.context).load(imgUri).into(img)
}
@BindingAdapter("SetABg")
fun changeImg2(img:ShapeableImageView,imgUri:String?){
    //img.setImageDrawable(imgres)
    img.setImageURI(imgUri?.toUri())
    Glide.with(img.context).load(imgUri).into(img)
}
@BindingAdapter("SendImg")
fun sendImage(img:ImageView,imgUri: String?){
    imgUri?.toUri()
    Glide.with(img.context).load(imgUri).into(img)
}

@BindingAdapter("Error")
fun setError(txtlayout:TextInputLayout,msg:String?){
    txtlayout.error=msg
}