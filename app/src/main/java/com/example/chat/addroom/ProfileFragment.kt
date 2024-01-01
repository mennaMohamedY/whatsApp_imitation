package com.example.chat.addroom

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.bottomsheet
import com.example.chat.databinding.FragmentProfileBinding
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.example.chat.newchat.NewChatActivity
import com.example.chat.signin.SignInActivity
import com.example.chat.signin.UserProfileObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {

    lateinit var addRoomBinding: FragmentProfileBinding
    lateinit var addRoomViewModel: AddRoomViewModel
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addRoomViewModel = ViewModelProvider(this).get(AddRoomViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addRoomBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return addRoomBinding.root
        //return inflater.inflate(R.layout.fragment_my_rooms, container, false)
    }

    //
    override fun onStart() {
        super.onStart()
        subScribeToLiveData()
    }

    override fun onResume() {
        super.onResume()
        subScribeToLiveData()
    }

    override fun onPause() {
        super.onPause()
        subScribeToLiveData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addRoomBinding.vm = addRoomViewModel

        getUsersandCheck()
        //addRoomViewModel.profilepic.value=UserProvider.user?.userimgUri?.toUri()

        subScribeToLiveData()
        addRoomBinding.edit.setOnClickListener({
            showBottomSheetDialog()
        })
        firebaseStorage = FirebaseStorage.getInstance()
        storageRef = firebaseStorage.reference
        addRoomBinding.exitApp.setOnClickListener({
           val fb= FirebaseAuth.getInstance()
            fb.signOut()
            val intent=Intent(requireContext(),SignInActivity::class.java)
            startActivity(intent)
        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val selectedimg = data.data
            addRoomBinding.pp.setImageURI(selectedimg)
        }
    }

    private fun subScribeToLiveData() {
        Log.e(
            "userprovider",
            "${UserProvider.user?.userimgUri}   , ${UserProvider.user?.userProfilePic}"
        )
        val imgprofile = imsprovider.immg
        val imgobserve = imsprovider.f
        imgobserve.observe(this) {
            if (!it) {
                addRoomBinding.pp.setImageResource(R.drawable.ic_baseline_person_24)
            } else {
                Glide.with(this).load(imgprofile.value).into(addRoomBinding.pp)
                Log.e("value", "${it}")
            }
        }

        imgprofile.observe(this) {
            addRoomViewModel.profilepic.set(it)
            if (UserProvider.user?.userID != null)
                FireStoreUtiles().updateUser(UserProvider.user?.userID!!, true, it)
        }
    }

    private fun showBottomSheetDialog() {
        val botomSheetDialog: BottomSheetDialogFragment = bottomsheet()
        botomSheetDialog.show(fragmentManager!!, "")
        onResume()

    }

    fun getUsersandCheck() {
        FireStoreUtiles().gettALlUsers().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.forEach {
                    val un = it.toObject(User::class.java)
                    if (un.userID == UserProvider.user?.userID) {
                        UserProvider.user = un
                        Log.e("un", "found it")
                        Log.e(
                            "userprovider2",
                            "${UserProvider.user?.userID}   , ${UserProvider.user?.userName}"
                        )
                        Log.e(
                            "userprovider",
                            "${UserProvider.user?.userimgUri}   , ${UserProvider.user?.userProfilePic}"
                        )
                        addRoomViewModel.profilepic.set(un.userimgUri?.toUri())
                        addRoomViewModel.username.set(un.userName)
                        addRoomViewModel.phonenum.set(un.userPhoneNum.toString())
                        addRoomViewModel.status.set(un.status)
                    }
                }
            }
        }
    }

}
