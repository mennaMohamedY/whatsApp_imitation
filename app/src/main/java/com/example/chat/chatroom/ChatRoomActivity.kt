package com.example.chat.chatroom

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.chat.R
import com.example.chat.databinding.ActivityChatRoomBinding
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

class ChatRoomActivity : AppCompatActivity(),ChatNavigator {
    lateinit var chatViewBinding:ActivityChatRoomBinding
    lateinit var chatViewModel:ChatRoomViewModel
    var chatAdapter= ChatAdapter(mutableListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_chat_room)
        chatViewBinding=ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(chatViewBinding.root)

        chatViewModel=ViewModelProvider(this).get(ChatRoomViewModel::class.java)
        chatViewBinding.vm=chatViewModel
        //chatViewBinding.chatRecyclerview.adapter=chatAdapter
        chatViewBinding.chatRecyclerview.adapter=chatAdapter
        subscribeToLiveData()
        scrollToBtn()

        /*chatViewBinding.sendbtn.setOnClickListener({
            try2()
            //subscribeToLiveData()

        })
        */
    }
    fun subscribeToLiveData(){
        chatViewBinding.chatRecyclerview.smoothScrollToPosition(chatAdapter.itemCount+1)

        FireStoreUtiles().getAllChat().addSnapshotListener(object :com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error !=null){
                    Toast.makeText(this@ChatRoomActivity,"BtnClicked but error happened", Toast.LENGTH_LONG).show()
                    showDialog("BtnClicked but error happened")
                }else{

                    value?.documentChanges?.forEach {
                        val msg= it.document.toObject(ChatDataClass::class.java)
                        chatAdapter.updatemsg(msg)
                        //chatViewModel.msgContext?.set(null)

                    } }
            }
        })
    }
    fun try2(){
        val emailSplitter=UserProvider.user?.userEmail?.split("@")
        val fstEmailName=emailSplitter?.get(0)
        val msgcontent=chatViewModel.msgContext?.get()?: "message1"
        val msgg=ChatDataClass(
            senderID = UserProvider.user?.userID,
            senderName = fstEmailName,
            messsageContent = msgcontent,
        )
        insertMsgToDB(msgg)
        //showmsgOnRecyclerView()
    }
    fun showmsgOnRecyclerView(){
        FireStoreUtiles().getAllChat().addSnapshotListener(object :EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error !=null){
                    Log.e("error","Apparently an error occured")
                }else{
                    value?.documentChanges?.forEach({
                        chatAdapter.updatemsg(it.document.toObject(ChatDataClass::class.java))
                    })
                    chatViewModel.msgContext?.set(null)
                }
            }

        })
    }
    fun insertMsgToDB(mesg:ChatDataClass){

        FireStoreUtiles().InsertMsgToDB(mesg).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this@ChatRoomActivity,"message is added successfuly",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@ChatRoomActivity,"something wnet wrong",Toast.LENGTH_LONG).show()

            }

        }
    }


    override fun onStop() {
        super.onStop()
        chatViewModel.listener?.remove()
    }


    override fun showToast(msg: String) {
        Toast.makeText(this,"${msg}",Toast.LENGTH_LONG).show()
    }

    var progressDialog:ProgressDialog?=null
    override fun showDialog(msg: String) {
        progressDialog=ProgressDialog(this)
        progressDialog?.setMessage(msg)
        progressDialog?.show()
    }

    override fun updatemsg(msg: ChatDataClass) {
        chatViewBinding.chatRecyclerview.adapter=chatAdapter
        chatAdapter.updatemsg(msg)
    }

    override fun listener(listen: ListenerRegistration) {
        TODO("Not yet implemented")
    }

    override fun scrollToBtn() {
        chatViewBinding.chatRecyclerview.smoothScrollToPosition(chatAdapter.itemCount+1)
    }
}