package com.example.chat.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.chat.databinding.SinglechatDesignBinding
import com.example.chat.models.User

class HomeAdapter(var users: MutableList<User?>?):Adapter<HomeAdapter.myChatsHolder>() {
    var onChatClickListener:OnChatClickListener?=null

    fun insertData(user: User?){
        //users?.add(user)
        users?.add(user)
        //notifyItemInserted(users!!.size)
        notifyItemInserted(users!!.size)
    }
    fun upDateData(user: MutableList<User?>?){
        users=user
        notifyDataSetChanged()
    }

    class myChatsHolder(var chatBinding:SinglechatDesignBinding):ViewHolder(chatBinding.root){
        fun bind(Usr:User){
            chatBinding.vm=Usr
            chatBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myChatsHolder {
        val chatBinding=SinglechatDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return myChatsHolder(chatBinding)
    }

    override fun onBindViewHolder(holder: myChatsHolder, position: Int) {
       var currentUser=users?.get(position)
        try{
           holder.bind(currentUser!!)
            holder.chatBinding.root.setOnClickListener {
                onChatClickListener?.OnChatClicked(currentUser,position)
            }
        }catch (e:Exception){
            Log.e("Exception","${e.localizedMessage}")
        }

//        if(ChatsProvider.usersID!!.contains(currentUser?.userID!!)){
//            holder.bind(currentUser)
//        }
//        for(i in 0..ChatsProvider.usersID?.size!!-1){
//            if(currentUser?.userID == ChatsProvider.usersID!![i]){
//                holder.bind(currentUser)
//            }
//        }
        //holder.chatBinding.pic.setImageResource(R.drawable.bg_menna)
//        holder.chatBinding.name.text="menna"
    //    holder.chatBinding.name.text=currentUser?.userName
        //Glide.with(holder.itemView.context).load(currentUser?.userimgUri).into(holder.chatBinding.pic)
//        if (currentUser.userimgUri.isNullOrEmpty()){
//            holder.chatBinding.pic.setImageResource(R.drawable.ic_baseline_person_24)
//        }
//        holder.chatBinding.name.setOnClickListener({
//            ShowImgUri?.showImgUri(currentUser!!,position)
//        })

    }
    interface OnChatClickListener{
        fun OnChatClicked(Usr: User,position: Int){

        }
    }

    override fun getItemCount(): Int {
        return  users?.size?:0
    }
}