package com.example.chat.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.chat.R
import com.example.chat.databinding.FragmentHomeBinding
import com.example.chat.models.FireStoreUtiles
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.example.chat.myroomsfragment.usersProvider
import com.example.chat.newchat.NewChatActivity
import com.example.chat.singlechatchat.SingleChatChatActivity
import com.example.chat.singlechatchat.SingleChatChatNavigator
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() ,SingleChatChatNavigator{

    lateinit var homfragBinding: FragmentHomeBinding
    lateinit var homefragVM:HomeFraViewModel
    var homeAdapter= HomeAdapter(mutableListOf())

    var userChatsID= mutableListOf<String>()
    var usersUsers= mutableListOf<User?>()
    var myChats=MutableLiveData<User>()
    var allUsrs=MutableLiveData<List<User>>()
    var listener:ListenerRegistration?=null
    var distinctUsers:List<User?>? = null
    var distinctUsers2:MutableLiveData<List<User?>>? = null


    var chatsMine:MutableList<User?>? = null
    var mydistinctChats:MutableLiveData<MutableList<User?>>?=null

    var try1:MutableList<User?>? =null
    var try2:List<User?>? =null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homefragVM=ViewModelProvider(this).get(HomeFraViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homfragBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return homfragBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homefragVM.navigator=this
        homfragBinding.chatsRV.adapter=homeAdapter
        //getAllUsersd()

        homfragBinding.getcontents.setOnClickListener({
            val intent= Intent(requireContext(), NewChatActivity::class.java)
            startActivity(intent)
        })
        //getChatz2()
        //subscribeToLiveData()

        homeAdapter.onChatClickListener=object :HomeAdapter.OnChatClickListener{
            override fun OnChatClicked(Usr: User, position: Int) {
                super.OnChatClicked(Usr, position)
                val intent=Intent(requireContext(),SingleChatChatActivity.getInstance(Usr)::class.java)
                startActivity(intent)
            }
        }

        homfragBinding.search.setOnQueryTextListener(object :OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                //filteredList(newText!!)
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getAllUsersd()
        getChatz2()
        //n()
        //subscribeToLiveData()
        subscribeToLiveData2()
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
        listener=null
    }
//    fun filteredList(Query:String){
//        if (Query !=null){
//            //incase you are searching the movies genre
//            val filteredList= mutableListOf<User?>()
//            for(i in usersProvider.users!!){
//                if (i?.userName?.toLowerCase(Locale.ROOT)!!.contains(Query)){
//                    filteredList.add(i)
//                }
//            }
//            if (filteredList == null){
//                homfragBinding.notFound.visibility=View.VISIBLE
//            }else{
//                homeAdapter.upDateData(filteredList)
//                homfragBinding.chatsRV.adapter=homeAdapter
//            }
//        }
//    }

//    fun subscribeToLiveData(){
//        Log.e("subscribe","${try1?.size}")

//        myChats.observe(this){ uzer->
//
//            val cu=usersUsers.find {
//                uzer.userID==it?.userID
//            }
//           homeAdapter.insertData(cu)
//        }
//       try {
//           for (i in 0..distinctUsers2?.value?.size!!-1){
//               for(j in 0..distinctUsers?.size!!-1){
//                   if (distinctUsers!!.get(j)?.userID == distinctUsers2!!.value!!.get(i)?.userID){
//                       homeAdapter.insertData(distinctUsers!!.get(i))
//                   }
//               }
//           }
//       }catch (e:Exception){
//
//       }
//       try2= ChatsProvider.emptyUsers.distinctBy {
//            it?.userID
//           Log.e("try1","${ChatsProvider.emptyUsers?.size}")
//        }
//        homeAdapter.upDateData(try2 as MutableList<User?>?)
//        ChatsProvider.emptyU.observe(this){
//          try {
//              var exist=false
//              for (i in 0..usersUsers.size-1){
//                  for(j in 0..it?.size!!-1){
//                      if(usersUsers[i]?.userID == it[j]?.userID ){
//                          exist=true
//                      }
//                  }
//                  Log.e("try1","${usersUsers[i]?.userID}")
//                  homeAdapter.insertData(usersUsers[i])
//              }
//          }catch (e:Exception){
//
//          }
//        }
//    }

    fun subscribeToLiveData2(){
        Log.e("subscribeToLiveData2()","subscribeToLiveData2()")
        FireStoreUtiles().getAllChatzz(UserProvider.user?.userID!!).addOnCompleteListener {
            if (it.isSuccessful){
                val resu=it.result.toObjects(User::class.java)
                homeAdapter.upDateData(resu)
            }
        }
    }


    fun n() {
        homfragBinding.chatsRV.adapter=homeAdapter

        FireStoreUtiles().getAllChatzzHomeFrag(UserProvider.user?.userID!!).addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("TAG", "listen:error", e)
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        val docAdded=dc.document.toObject(User::class.java)
                        Log.e("n","${dc.document.data}")
                        val user=dc.document.toObject(User::class.java)
                      //  homeAdapter.insertData(user)
                        myChats.value=dc.document.toObject(User::class.java)

//
//                        if(!(ChatsProvider.emptyUsers.contains(user))){
//                            ChatsProvider.emptyUsers.add(user)
//                            myChats.value=dc.document.toObject(User::class.java)
//                            homeAdapter.insertData(user)
//                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        Log.d("TAG", "Modified city: ${dc.document.data}")
                        val docChanged=dc.document.data
                    }
                    DocumentChange.Type.REMOVED -> Log.d("TAG", "Removed city: ${dc.document.data}")
                    else -> {
                        //do something
                    }
                }
            }
        }
    }
    fun getChatz2() {
        if(listener == null){
            listener=FireStoreUtiles().getAllChatzzHomeFrag(UserProvider.user?.userID!!).addSnapshotListener { value, error ->
                if (error != null){
                    showProgressDialog(error.localizedMessage)
                }
                else{
                    value?.documentChanges?.forEach {
                        val ur=it.document.toObject(User::class.java)
                        val chat=it.document.toObject(User::class.java)
                        ChatsProvider.emptyUsers.add(chat)
                        chatsMine?.add(chat)
                        mydistinctChats?.value?.add(chat)
                        //homeAdapter.insertData(chat)
                        try1?.add(chat)
                        Log.e("chatz","${ChatsProvider.emptyUsers.size}")
                        ChatsProvider.emptyU.value?.add(chat)


//                        if(!(ChatsProvider.emptyUsers2.contains(chat)) ){
//                            var exist=false
//                            for (i in 0..ChatsProvider.emptyUsers2.size-1){
//                                if (ChatsProvider.emptyUsers2.get(i)?.userID == chat.userID){
//                                    exist=true
//                                }
//                            }
//                            if (exist==false){
//                                ChatsProvider.emptyUsers2.add(chat)
//                                homeAdapter.insertData(chat)
//                            }
//                            //ChatsProvider.emptyUsers2.
//                           // ChatsProvider.emptyUsers2.add(chat)
//                            //myChats.value=dc.document.toObject(User::class.java)
//                            //homeAdapter.insertData(chat)
//                        }
//                    }
                }
            }
        }
        distinctUsers2?.value= mydistinctChats?.value?.distinctBy {
            it?.userID
        }
    }}

    //list contains all the users we have in firestore
    fun getAllUsersd() {
        FireStoreUtiles().gettALlUsers().addOnCompleteListener {
            if(it.isSuccessful){
                it.result.forEach {
                    usersUsers.add(it.toObject(User::class.java))
                }
        }
            Log.e("users","${usersUsers.size}")
        }
        distinctUsers=usersUsers.distinctBy {
            it?.userID
        }
    }

    fun getAllUsers(){
        //usersUsers.clear()
        //usersUsers= mutableListOf<User?>()
        FireStoreUtiles().gettALlUsers().addOnCompleteListener {
            if(it.isSuccessful){
                val users=it.result.toObjects(User::class.java)
                allUsrs.value=it.result.toObjects(User::class.java)
                for (i in 0..users.size-1){
                    if(userChatsID.contains(users[i].userID)){
                        usersUsers.add(users[i])
                        Log.e("Userssssouff","${users[i]}")
                        //Log.e("Userssssouff2","${usersUsers[0]}")
                    }
                } }
            else{
                Toast.makeText(requireContext(), "Error Occured , ${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
        val distinctUser=usersUsers.distinctBy { it?.userID }
        //homeAdapter.upDateData(distinctUser)
        usersProvider.users=distinctUser
    }

    fun addIDs(){
        userChatsID.clear()
        FireStoreUtiles().getAllChatzz(UserProvider.user?.userID!!)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    it.result.forEach {
                        userChatsID.add(it.id)
                        Log.e("ids","${it.id}")
                    }
                }
            }
        ChatsProvider.usersID=userChatsID
    }

    private fun showProgressDialog(error:String) {
        val progDialog=ProgressDialog(requireContext())
        progDialog.setTitle("Error ${error}")
        progDialog.show()
    }

    override fun showMsg(msg: String) {
        Toast.makeText(requireContext(), "home ${msg}", Toast.LENGTH_SHORT).show()
        Log.e("home", "${msg}")
    }

    fun hideTxtt() {
        homfragBinding.nochatsyet.visibility=View.INVISIBLE
    }

    override fun hideTxt() {
        homfragBinding.nochatsyet.visibility=View.INVISIBLE
    }

}