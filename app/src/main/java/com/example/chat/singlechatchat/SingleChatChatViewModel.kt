package com.example.chat.singlechatchat

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat.chatroom.ChatDataClass
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*

class SingleChatChatViewModel:ViewModel() {
    var profilpic=ObservableField<Uri>()
    var name=ObservableField<String>()
    var msg=ObservableField<String>()
    var msgs=MutableLiveData<List<ChatDataClass?>?>()
    var getter_Uid=ObservableField<String>()
    var sender_Uid=ObservableField<String>()
    var calendar:Calendar?=null
    var senderNAme=ObservableField<String>()
    var navigator:SingleChatChatNavigator?=null

    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())

    fun OnSendClickListener(){
        if(msg.get().isNullOrEmpty()){
            return
        }
        else{
            navigator?.showMsg("Send Btn Clicked!")
          // insertMsgToDB()
            insertToDb2()
           // getMsgsFromDB()
        }
    }

    fun insertMsgToDB() {
        calendar = Calendar.getInstance()
        var timeFormater = SimpleDateFormat("HH:mm")
        var currentTime = timeFormater.format(calendar!!.time)

        var timeFormater2 = SimpleDateFormat("yyyy-MM-dd HH:mm")
        var currentTime2 = timeFormater2.format(calendar!!.time)

        //ahna hna defna fl user aly ba3t say msln ana b3t l aya fkda dft 3ndy collection feha al id bta3 aya
        // w gwa alcollection de fe documents feha almsgs bt3tna

        var ChatMSg = ChatDataClass(
            senderID = sender_Uid.get(),
            senderName = senderNAme.get(),
            tm = currentTime,
            messsageContent = msg.get(),
            getter_name = name.get(),
            tie = currentDate
        )
        FireStoreUtiles().insertChatToUsers(
            senderid = sender_Uid.get()!!,
            getter_uid = getter_Uid.get()!!,
            msgg = ChatMSg
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    navigator?.showMsg("Succeedd :D")
                    msg.set(null)
                } else {
                    navigator?.showMsg(it.exception!!.localizedMessage)

                }
            }


        //dlw2ty 3yza a3ml kman al3ks bm3na an 3nd aya b2a yt3ml collection feha alid bta3y
        //w gwa alcollection de ahot documents gwaha almsgs f bltaly almsgs tkoun byna 3ndy w 3ndha hya kman lma tfth
        //mogrd bs any h2lb alid l2n fl FireStoreUtile aly 3ndy ana hata al sender id da alrefrence aly bshawr 3leh
        //fna 3yza yshawr 3ala alid bta3 aya msh alid bta3y

        FireStoreUtiles().insertChatToUsers(
            senderid = getter_Uid.get()!!,
            getter_uid = sender_Uid.get()!!,
            msgg = ChatMSg
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    navigator?.showMsg("Succeedd :D")
                    msg.set(null)
                } else {
                    navigator?.showMsg(it.exception!!.localizedMessage)
                }
            }
        val ucer = User(
            userID = getter_Uid.get(),
            userName = name.get(),
            userimgUri = profilpic.get().toString()
        )

        FireStoreUtiles().insertChatToUsersTry2(
            senderid = sender_Uid.get()!!,
            getter_uid = getter_Uid.get()!!,
            ucer
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    navigator?.showMsg("Succeedd :D")
                    msg.set(null)
                } else {
                    navigator?.showMsg(it.exception!!.localizedMessage)
                }
            }
    }

    fun insertToDb2(){

        //to get the date when the message is sent
        calendar = Calendar.getInstance()
        var timeFormater = SimpleDateFormat("HH:mm:ss")
        var currentTime = timeFormater.format(calendar!!.time)
        var timeFormater2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var currentTime2 = timeFormater2.format(calendar!!.time)

        //
        var ChatMSg = ChatDataClass(
            senderID = sender_Uid.get(),
            senderName = senderNAme.get(),
            tm = currentTime,            //alw2t byhng msh bytl3 mzbot 3lshan kda astkhdm tm w tie bgrb anhy hyzbot mmkn amsh ay wahda mnhm
            messsageContent = msg.get(),
            getter_name = name.get(),     //alshkhs aly hb3tloh almsg w da almfroud hgeb aldata bt3to mn alactivity lma bdos 3leh bygeele aldata mn aladapter
            tie  = currentDate,          // bs halyn aly bstkhdmha hya de aldata btrg3le mtrtba 3ala hsb alw2t tie
        )
        val ucer = User(
            userID = getter_Uid.get(),
            userName = name.get(),
            userimgUri = profilpic.get().toString(),
            getterName = name.get(),
            senderName = senderNAme.get(),
        )
        FireStoreUtiles().insertMsgToChat(senderid = sender_Uid.get()!!, getter_uid = getter_Uid.get()!!,ChatMSg,ucer)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    navigator?.showMsg("Succeedd :D")
                    msg.set(null)
                }
                else{
                    navigator?.showMsg(it.exception!!.localizedMessage)
                }
            }
        FireStoreUtiles().updatelastMessage(senderID = sender_Uid.get()!!, getter_uid = getter_Uid.get()!!,msg.get()!!)
        FireStoreUtiles().updatelastMessage(senderID = getter_Uid.get()!!, getter_uid = sender_Uid.get()!!,msg.get()!!)


        val ucer2 = User(
            userID = sender_Uid.get(),
            userName = UserProvider.user?.userName,
            userimgUri = UserProvider.user?.userimgUri.toString(),
        )

        FireStoreUtiles().insertMsgToChat(senderid = getter_Uid.get()!!, getter_uid = sender_Uid.get()!!,ChatMSg,ucer2)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    navigator?.showMsg("Succeedd :D")
                    msg.set(null)
                }
                else{
                    navigator?.showMsg(it.exception!!.localizedMessage)
                }
            }
    }

//    fun getMsgsFromDB(){
//        FireStoreUtiles().getAllMsgs(getter_Uid.get()!!,sender_Uid.get()!!).addOnCompleteListener {
//            if (it.isSuccessful){
//               // msgs.set(it.result.toObjects(ChatDataClass::class.java))
//                msgs.value=it.result.toObjects(ChatDataClass::class.java)
//                Log.e("getterID","${getter_Uid.get()}")
//            }
//        }
//    }


}