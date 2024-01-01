package com.example.chat.home

import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.chat.chatroom.ChatDataClass
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.example.chat.singlechatchat.SingleChatChatNavigator
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

class HomeFraViewModel:ViewModel() {
    var navigator:SingleChatChatNavigator?=null
    var user=ObservableField<User>()
    var homeAdapter=HomeAdapter(mutableListOf())
    var pp=ObservableField<Uri>()
    var date=ObservableField<String>()
    var notification=ObservableField<String>()


//    fun getAllChats(){
//        FireStoreUtiles().getAllChatsInUser(UserProvider.user?.userID.toString())
//            .addOnCompleteListener {
//                if (it.isSuccessful){
//                    val useer=it.result.
//                    val users=it.toObject(User::class.java)
//                    navigator?.hideTxt()
//                    homeAdapter.upDateData(users)
//                    navigator?.showMsg("succeed ${users.userID} !")
//                }
//            }
//        FireStoreUtiles().getAllChatsInUser(UserProvider.user?.userID!!)
//            .addSnapshotListener { value, error ->
//                if(error !=null){
//                    navigator?.showMsg("${error.localizedMessage} !")
//                }else{
//                    value?.forEach {
//                        val users=it.toObject(User::class.java)
//                        navigator?.hideTxt()
//                        homeAdapter.upDateData(users)
//                        navigator?.showMsg("succeed ${users.userID} !")
//
//                    }
//                }
//            }
//
//    }
//    fun getChats(){
//        FireStoreUtiles().getAllChatz(UserProvider.user?.userID!!)
//            .addOnCompleteListener {
//                if (it.isSuccessful){
//                    it.result.forEach {
//                        val ussrs=it.toObject(User::class.java)
//                        homeAdapter.upDateData(ussrs)
//                        navigator?.showMsg("Succeed ${ussrs.userID}")
//                        pp.set(ussrs.userimgUri?.toUri())
//                    }
//                }
//            }
//    }

}