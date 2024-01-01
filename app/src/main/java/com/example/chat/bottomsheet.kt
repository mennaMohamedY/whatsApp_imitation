package com.example.chat

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chat.addroom.AddRoomViewModel
import com.example.chat.addroom.ProfileFragment
import com.example.chat.addroom.imsprovider
import com.example.chat.databinding.BottomSheetDialogBinding
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.UserProvider
import com.example.chat.signin.UserProfileObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class bottomsheet: BottomSheetDialogFragment() {
   // lateinit var  bottomFragBinding:BottomSheetDialogBinding
    lateinit var cancel:ImageView
    lateinit var takephoto:ConstraintLayout
    lateinit var chosephoto:ConstraintLayout
    lateinit var useAvatar:ConstraintLayout
    lateinit var deletphoto:ConstraintLayout
    lateinit var imgpp:ImageView
    lateinit var addRoomViewModel : AddRoomViewModel
    lateinit var bottomFragBinding:BottomSheetDialogBinding
    var frag=ProfileFragment()
    lateinit var firebaseStorage:FirebaseStorage
    lateinit var storageRef:StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addRoomViewModel=ViewModelProvider(this).get(AddRoomViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomFragBinding=DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.bottom_sheet_dialog,container,false)
        //return  inflater.inflate(R.layout.bottom_sheet_dialog,container,false)
        return bottomFragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
//        bottomFragBinding.vm=addRoomViewModel
        cancel.setOnClickListener({
            Log.e("cancel","Btn Clicked")
            dismiss()
        })
        bottomFragBinding.vm=UserProfileObserver
        UserProfileObserver.botmsheetpp.value=UserProvider.user?.userimgUri?.toUri()




        deletphoto.setOnClickListener({
            imgpp.setImageResource(R.drawable.ic_baseline_person_24)
            Toast.makeText(requireContext(), "BtnClicked", Toast.LENGTH_SHORT).show()
            //imsprovider.bindImg=true
            //imsprovider.deletePhoto.value=true
            deleteFromStorageAndUpdateFireStore()
        })

        takephoto.setOnClickListener({
            val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,3)
        })

        useAvatar.setOnClickListener({
            getImgFromFirebaseStorage()

        })
    }

    private fun deleteFromStorageAndUpdateFireStore() {
        //here we need to delete the image from the firebase storage
        //then we will update the firebaseFireStore

        //first we need to get the user photo w n3ml refrenece 3leha y3ne nshawr 3leha 3lshan nl3'eha
        val imgRef = storageRef.child("images/"+UserProvider.user!!.userID )
        val progressDialog=ProgressDialog(requireContext())
        progressDialog.setTitle("Img Deleted From Storage")
        progressDialog.show()

        imgRef.delete().addOnSuccessListener {
            progressDialog.dismiss()
            notifyFireStore()
        }.addOnFailureListener {
            progressDialog.dismiss()
            Log.e("delet","Failed !,${it.localizedMessage}")
            Toast.makeText(requireContext(), "FAiled To Delete From Storage", Toast.LENGTH_SHORT).show()
        }
    }
    fun notifyFireStore(){
        //if error occured set the provider img to null

        //getImgFromFirebaseStorage()
        val imgRef = storageRef.child("images/person.jpg" )

        imgRef.downloadUrl.addOnSuccessListener {
            //Glide.with(requireContext()).load(it).into(imgpp)
            // addRoomViewModel.imgg.value=it
            //imsprovider.downloadfromstorage=it
            imsprovider.f.value=false
            //UserProfileObserver.imgURI.value=it
            imsprovider.immg.value=it
            //updateFireStoreUserData(it)

            //local storage (prefrences)
            UserProvider.user!!.userimgUri=it.toString()
            UserProvider.user!!.userProfilePic=false

            FireStoreUtiles().updateUser(UserProvider.user?.userID!!,false,it)

        }.addOnFailureListener {
            Toast.makeText(requireContext(),"something wnt wrong",Toast.LENGTH_LONG).show()

        }
    }

    private fun getImgFromFirebaseStorage(){

        val imgRef = storageRef.child("images/"+UserProvider.user!!.userID )

        imgRef.downloadUrl.addOnSuccessListener {

            Glide.with(requireContext()).load(it).into(imgpp)
           // addRoomViewModel.imgg.value=it
            imsprovider.immg.value=it
            imsprovider.f.value=true
            UserProfileObserver.imgURI.value=it
            updateFireStoreUserData(it)
            UserProvider.user!!.userimgUri=it.toString()


        }.addOnFailureListener {
            Toast.makeText(requireContext(),"something wnt wrong",Toast.LENGTH_LONG).show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //parentFragment!!.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null){
            val selectedimg = data.data
            imgpp.setImageURI(selectedimg)

            imsprovider.newImg=true
            imsprovider.selectedimg=selectedimg
            uploadPicToFibaseStorage()

        }
    }

    private fun uploadPicToFibaseStorage() {
        val progressDialog=ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading Image...")
        progressDialog.show()
        imsprovider.f.value=true

// Create a reference to "mountains.jpg"
        //val randomKey=UUID.randomUUID().toString()
        val mountainsRef = storageRef.child("images/" + UserProvider.user!!.userID)

// Create a reference to 'images/mountains.jpg'
        val mountainImagesRef = storageRef.child("images/mountains.jpg")

// While the file names are the same, the references point to different files
        //mountainsRef.name == mountainImagesRef.name //
        mountainsRef.putFile(imsprovider.selectedimg!!)
            .addOnSuccessListener {
                Snackbar.make(requireView(),"Image Uploaded",Snackbar.LENGTH_LONG).show()
                progressDialog.dismiss()
                getImgFromFirebaseStorage()
            }.addOnFailureListener({
                progressDialog.dismiss()
                imsprovider.f.value=true
                Toast.makeText(requireContext(),"Failed To Upload" , Toast.LENGTH_LONG).show()

            })
            .addOnProgressListener {
                val progressPercent=(100.00 * it.bytesTransferred)/(it.totalByteCount)
                progressDialog.setMessage("Progress ${progressPercent} %")
            }

    }

    private fun updateFireStoreUserData(it:Uri) {
        FireStoreUtiles().updateUser(UserProvider.user?.userID!!,true,it)
        UserProfileObserver.profilePicChanged.value=true
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onResume()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            val selectedimg = data.data
//            imgpp.setImageURI(selectedimg)

//            val selectedImg=intent.data
//            imsprovider.newImg=true
//            imsprovider.selectedimg=selectedImg
//            imgpp.setImageURI(selectedImg)
//            val intent3=Intent(requireContext(),ProfileFragment::class.java)
//            startActivity(intent3)

      //  }
    //}





    private fun initViews() {
        cancel=view!!.findViewById(R.id.cancel)
        takephoto=view!!.findViewById(R.id.takephoto)
        chosephoto=view!!.findViewById(R.id.choosephoto)
        useAvatar=view!!.findViewById(R.id.useavatar)
        deletphoto=view!!.findViewById(R.id.deletphoto)
        imgpp=view!!.findViewById(R.id.img)
        firebaseStorage=FirebaseStorage.getInstance()
        storageRef=firebaseStorage.reference


    }
}