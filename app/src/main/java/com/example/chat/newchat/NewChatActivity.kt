package com.example.chat.newchat

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.databinding.ActivityNewChatBinding
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.example.chat.myroomsfragment.usersProvider
import com.example.chat.signin.UserProfileObserver
import com.example.chat.singlechatchat.SingleChatChatActivity
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class NewChatActivity : AppCompatActivity() {
    lateinit var newChatBinding: ActivityNewChatBinding
    lateinit var newchatVM: NewChatViewModel
    var newchatsAdapter = NewChatsAdapter(listOf())
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var imgsRef: StorageReference
    var initUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_new_chat)
        newChatBinding = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(newChatBinding.root)

        newchatVM = ViewModelProvider(this).get(NewChatViewModel::class.java)
        firebaseStorage = FirebaseStorage.getInstance()
        imgsRef = firebaseStorage.reference


        newChatBinding.newchatRv.adapter = newchatsAdapter
        getAllUsersFromFireStore()
        Log.e("users", "${UserProfileObserver.allUsers?.size}")
        newChatBinding.cancell.setOnClickListener({
            finish()
        })

        newchatsAdapter.onUserProfileClickListener =
            object : NewChatsAdapter.OnUSerProfileClickListener {
                override fun OnUserProfileClick(user: User, position: Int) {
                    super.OnUserProfileClick(user, position)
                    Log.e("NewChatActivity","${user.userID},${user.userName}")
                    val intent = Intent(this@NewChatActivity, SingleChatChatActivity.getInstance(user)::class.java)
                    //intent.putExtra("user", user)
                    startActivity(intent)
                }
            }
        newChatBinding.search.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText!!)
                return true
            }

        })
    }
    fun filterList(Query:String){
        if (Query !=null){
            //incase you are searching the movies genre
            val filteredList= mutableListOf<User?>()
            for(i in usersProvider.allUsers!!){
                if (i?.userName?.toLowerCase(Locale.ROOT)!!.contains(Query)){
                    filteredList.add(i)
                }
            }
            if (filteredList == null){
                newChatBinding.notFound.visibility=View.VISIBLE
            }else{
                newchatsAdapter.updateData(filteredList)
                newChatBinding.newchatRv.adapter=newchatsAdapter

            }
        }

    }

    private fun getAllUsersFromFireStore() {
        FireStoreUtiles().gettALlUsers().addOnCompleteListener {
            if (it.isSuccessful) {
                val allUsers = it.result.toObjects(User::class.java)
                UserProfileObserver.allUsers = it.result.toObjects(User::class.java)
                newchatsAdapter.updateData(allUsers)
                usersProvider.allUsers=allUsers
            } else {
                showProgressDialog(it.exception!!.localizedMessage)
            }
        }
    }

    fun ObserveUpdated() {
        FireStoreUtiles().getAllUsers().addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    showProgressDialog(error.localizedMessage)
                } else {
                    value?.documentChanges?.forEach {
                        val updatedUser = it.document.toObject(User::class.java)
                        UserProfileObserver.updatedUser = it.document.toObject(User::class.java)
                        //newchatsAdapter.updateData(userr)
                    }
                }
            }
        })
    }

    fun showProgressDialog(msg: String) {
        val progressDialog: ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("failed ${msg}")
        progressDialog.setTitle("Error occured while getting users from firestore")
        Log.e("Failure", "Failed ${msg}")
        progressDialog.show()
    }
}
