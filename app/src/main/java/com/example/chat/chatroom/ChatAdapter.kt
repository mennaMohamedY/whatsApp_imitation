package com.example.chat.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat.databinding.SingleMymessagesDesignBinding
import com.example.chat.databinding.SingleOthermessagesDesignBinding
import com.example.chat.models.UserProvider

class ChatAdapter (var Messages:MutableList<ChatDataClass>):Adapter<RecyclerView.ViewHolder>(){

    val myMessages:Int=100
    val otherMessages:Int=200

    fun updatemsg(msg:ChatDataClass){
        Messages.add(msg)
        notifyItemInserted(Messages.size)
    }

    class myChatHolder(val singleChatBinding:SingleMymessagesDesignBinding):ViewHolder(singleChatBinding.root){
         fun bind(messag:ChatDataClass){
             singleChatBinding.myMessage=messag
             singleChatBinding.invalidateAll()
         }
    }
    class otherChatHolder(val OtherChatBinding:SingleOthermessagesDesignBinding):ViewHolder(OtherChatBinding.root){
        fun bind(messag: ChatDataClass){
            OtherChatBinding.otherMsgs=messag
            OtherChatBinding.invalidateAll()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message=Messages.get(position)
        if(message.senderID == UserProvider.user?.userID){
            return  myMessages
        }
        else{
            return otherMessages
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 100){
            val singleChatBinding= SingleMymessagesDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return myChatHolder(singleChatBinding)
        }else{
            val OtherChatBinding=SingleOthermessagesDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return otherChatHolder(OtherChatBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is myChatHolder ){
            holder.bind(Messages.get(position))
        }
        if(holder is otherChatHolder){
            holder.bind(Messages.get(position))
        }
    }

    override fun getItemCount(): Int {
        return Messages.size?:0
    }
}