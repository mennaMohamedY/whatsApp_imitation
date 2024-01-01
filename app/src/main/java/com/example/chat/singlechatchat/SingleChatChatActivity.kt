package com.example.chat.singlechatchat

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.chat.R
import com.example.chat.addroom.imsprovider
import com.example.chat.chatroom.ChatDataClass
import com.example.chat.databinding.ActivitySingleChatChatBinding
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.example.chat.signin.UserProfileObserver
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class SingleChatChatActivity : AppCompatActivity(), SingleChatChatNavigator {
    lateinit var chatChatBinding: ActivitySingleChatChatBinding
    lateinit var chatChatVM: SingleChatChatViewModel
    var msgsAdapter = ChatsAdapter(mutableListOf())
    var imgUri=MutableLiveData<Uri>()
    val calendar = Calendar.getInstance()
    //var timeFormater = SimpleDateFormat("HH:mm")
    //var timeFormater2 = SimpleDateFormat("yyyy-MM-dd HH:mm ")

    //var currentTime = timeFormater.format(calendar!!.time)
    //var currentTime2 = timeFormater2.format(calendar!!.time)
    //val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    //val currentDate = sdf.format(Date())
    var timeFormater = SimpleDateFormat("HH:mm:ss")
    var currentTime = timeFormater.format(calendar!!.time)

    lateinit var storage: FirebaseStorage
    lateinit var storageRef:StorageReference

    companion object {
        var curentUser: User? = null
        fun getInstance(currentUser: User): SingleChatChatActivity {
            curentUser=currentUser
            return SingleChatChatActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatChatBinding = ActivitySingleChatChatBinding.inflate(layoutInflater)
        setContentView(chatChatBinding.root)
//        curentUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra("user", User::class.java)
//        } else intent.getParcelableExtra("user") as User?
        chatChatVM = ViewModelProvider(this).get(SingleChatChatViewModel::class.java)
        chatChatBinding.vm = chatChatVM

        chatChatVM.profilpic.set(curentUser?.userimgUri?.toUri())
        chatChatVM.name.set(curentUser?.userName)
        chatChatBinding.back.setOnClickListener({
            finish()
        })
        storage= FirebaseStorage.getInstance()
        storageRef=storage.reference


        chatChatVM.sender_Uid.set(UserProvider.user?.userID)
        chatChatVM.getter_Uid.set(curentUser?.userID)
        chatChatVM.name.set(curentUser?.userName)
        chatChatVM.senderNAme.set(UserProvider.user?.userName)
        chatChatVM.navigator = this
        chatChatBinding.msgsRv.adapter = msgsAdapter
        //getMsgsFromDB()
      //  msgsAdapter.upDateMsgs(chatChatVM.msgs.get())
        //chatChatVM.getMsgsFromDB()
        subscribeToLiveData()
        chatChatBinding.sendimg.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,3)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data !=null){
            val selectedImg=data.data
            imgsProvider.sentImg=selectedImg
            uploadPicToFibaseStorage()
    }}

    fun uploadPicToFibaseStorage() {
        val progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Uploading Image...")
        progressDialog.show()
        imsprovider.f.value=true
        val randomKey=UUID.randomUUID().toString()
        val mountainsRef = storageRef.child("images/" + randomKey)
        // While the file names are the same, the references point to different files
        //mountainsRef.name == mountainImagesRef.name //
        mountainsRef.putFile(imgsProvider.sentImg!!)
            .addOnSuccessListener {
                Log.e("Succeed","image uploaded")
                progressDialog.dismiss()
                getImgFromFirebaseStorage(randomKey)
            }.addOnFailureListener({
                progressDialog.dismiss()
                Toast.makeText(this,"Failed To Upload" , Toast.LENGTH_LONG).show()
            })
            .addOnProgressListener {
                val progressPercent=(100.00 * it.bytesTransferred)/(it.totalByteCount)
                progressDialog.setMessage("Progress ${progressPercent} %")
            }
    }

    fun getImgFromFirebaseStorage(randomKey:String){

        val imgRef = storageRef.child("images/"+ randomKey)
        imgRef.downloadUrl.addOnSuccessListener {
            updateFireStoreUserData(it)
        }.addOnFailureListener {
            Toast.makeText(this,"something wnt wrong",Toast.LENGTH_LONG).show()
        }
    }

    fun updateFireStoreUserData(selectedImg:Uri){
        val chatmsg=ChatDataClass(senderName = UserProvider.user?.senderName, senderID = UserProvider.user?.userID
            , sendImg = true, imgUri = imgsProvider.sentImg.toString())
        //msgsAdapter.upDateMsgs(chatmsg)
        //imgUri.value=data.data
        Log.e("sid","${UserProvider.user!!.userID}")
        Log.e("gid","${curentUser!!.userID}")
        var ChatMSg = ChatDataClass(
            senderID = UserProvider.user?.userID,
            senderName = UserProvider.user?.userName,
            tm = currentTime,
            getter_name= curentUser?.getterName,
            sendImg = true,
            imgUri = selectedImg.toString(),
            tie = currentTime,
        )
        val ucer = User(
            userID = curentUser?.getterName,
            userName = curentUser?.userName,
        )
        FireStoreUtiles().insertMsgToChat(senderid = UserProvider.user?.userID!!, getter_uid = curentUser?.userID!!,ChatMSg,ucer)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this@SingleChatChatActivity, "yess", Toast.LENGTH_SHORT).show()
                    Log.e("sid","${UserProvider.user!!.userID}")
                    Log.e("gid","${curentUser!!.userID}")

                    Log.e("fs","img inserted")
                }
                else{
                    Toast.makeText(this, "${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }

        //insert in the other side
        val ucer2 = User(
            userID = UserProvider.user?.userID,
            userName = UserProvider.user?.userName,
        )
        FireStoreUtiles().insertMsgToChat(senderid = curentUser?.userID!!, getter_uid = UserProvider.user?.userID!! ,ChatMSg,ucer2)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this@SingleChatChatActivity, "yess", Toast.LENGTH_SHORT).show()
                    Log.e("sid","${UserProvider.user!!.userID}")
                    Log.e("gid","${curentUser!!.userID}")

                    Log.e("fs","img inserted")
                }
                else{
                    Toast.makeText(this, "${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun subscribeToLiveData(){
        chatChatBinding.msgsRv.smoothScrollToPosition(chatChatBinding.msgsRv.bottom)

        FireStoreUtiles().getAllMsgs(curentUser?.userID!!,UserProvider.user?.userID!!).addSnapshotListener(object :com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error !=null){
                    Toast.makeText(this@SingleChatChatActivity,"BtnClicked but error happened", Toast.LENGTH_LONG).show()
                    //showDialog("BtnClicked but error happened")
                }else{

                    value?.documentChanges?.forEach {
                        val msg= it.document.toObject(ChatDataClass::class.java)
                        msgsAdapter.upDateMsgs(msg)
                        //chatViewModel.msgContext?.set(null)
                    } }
            }
        })

//        imgUri.observe(this){
//            if (it !=null){
//                insertToDb2(it)
//            }
//        }
    }


//    fun getMsgsFromDB() {
//        FireStoreUtiles().getAllMsgs(chatChatVM.getter_Uid.get()!!).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val msgs = it.result.toObjects(ChatDataClass::class.java)
//                msgsAdapter.upDateMsgs(msgs)
//                Log.e("Succeed!23", "succeeded")
//            } else {
//                Log.e("Failed!23", "Failed")
//
//            }
//        }
//    }

    override fun showMsg(msg: String) {
        Toast.makeText(this, "${msg}", Toast.LENGTH_SHORT).show()
        Log.e("addMsgToChat", "${msg}")
    }

    override fun hideTxt() {
        TODO("Not yet implemented")
    }


}