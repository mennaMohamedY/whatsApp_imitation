package com.example.chat.chatroom

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.UserProvider
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.EventListener

class ChatRoomViewModel :ViewModel() {
    var msgContext=ObservableField<String>()
    var navigator:ChatNavigator?=null
    var calendr:Calendar?=null


    var listener:ListenerRegistration?=null

    fun onSendClickListener(){
        if(msgContext?.get().isNullOrEmpty()){
            return
        }else{
            navigator?.showToast("send btn is clicked")
            insertMsgToDB()
            navigator?.scrollToBtn()

            /* listener= FireStoreUtiles().getAllChat().addSnapshotListener(object :com.google.firebase.firestore.EventListener<QuerySnapshot>{
                  override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                      if (error !=null){
                          navigator?.showToast(error.localizedMessage)
                          navigator?.showDialog(error.localizedMessage)
                      }else{
                          value?.documentChanges?.forEach {
                              val msg= it.document.toObject(ChatDataClass::class.java)
                              navigator?.updatemsg(msg)
                              msgContext?.set(null)
                          }
                      }
                  }

              })*/
        }

    }


    fun insertMsgToDB(){

        val emailSplitter=UserProvider.user?.userEmail?.split("@")
        val fstEmailName=emailSplitter?.get(0)
        calendr=Calendar.getInstance()
        var timeFormater= SimpleDateFormat("HH:mm")
        var currentTime =timeFormater!!.format(calendr!!.time)


        val messge=ChatDataClass(
            senderName = fstEmailName,
            senderID = UserProvider.user?.userID,
            messsageContent = msgContext?.get(),
            tm = currentTime
            )
        FireStoreUtiles().InsertMsgToDB(messge).addOnCompleteListener {
            if(it.isSuccessful){
                navigator?.showToast("message is added successfuly")
                msgContext.set(null)
            }else{
                navigator?.showToast("error occured !")

            }

        }
    }
}