package com.example.chat.newchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.databinding.SinglenewchatDesignBinding
import com.example.chat.models.User
import com.example.chat.models.UserProvider
import com.example.chat.signin.UserProfileObserver
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class NewChatsAdapter(var usersData:List<User?>?):Adapter<NewChatsAdapter.newChatHolder>() {
    var onUserProfileClickListener:OnUSerProfileClickListener?=null
    var firebaseStorage: FirebaseStorage= FirebaseStorage.getInstance()
    var imgsRef: StorageReference=firebaseStorage.reference

    fun updateData(userData:List<User?>?){
        usersData=userData
        notifyDataSetChanged()
    }


    class newChatHolder(val itemview:SinglenewchatDesignBinding):ViewHolder(itemview.root){
        fun bind(item:User){
            itemview.vm=item
            itemview.executePendingBindings()
        }
        fun bind2(provider:UserProfileObserver){
            itemview.vmm=provider
            itemview.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): newChatHolder {
        val newchatBinding=SinglenewchatDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return newChatHolder(newchatBinding)
    }

    override fun onBindViewHolder(holder: newChatHolder, position: Int) {
        val currentItem=usersData?.get(position)
        holder.bind(currentItem!!)
        holder.itemView.setOnClickListener({
            onUserProfileClickListener?.OnUserProfileClick(currentItem,position)
        })
        if (currentItem.userProfilePic==false){
            holder.itemview.pic.setImageResource(R.drawable.ic_baseline_person_24)
        }else{
            Glide.with(holder.itemView.context).load(currentItem.userimgUri).into(holder.itemview.pic)
        }
    }
    interface OnUSerProfileClickListener{
        fun OnUserProfileClick(user: User,position: Int){}
    }

    override fun getItemCount(): Int {
        return usersData?.size?:0
    }
}