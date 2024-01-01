package com.example.chat.singlechatchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat.chatroom.ChatAdapter
import com.example.chat.chatroom.ChatDataClass
import com.example.chat.databinding.SingleMyimgsDesignBinding
import com.example.chat.databinding.SingleMymessagesDesignBinding
import com.example.chat.databinding.SingleOtherimgsDesignBinding
import com.example.chat.databinding.SingleOthermessagesDesignBinding
import com.example.chat.models.UserProvider

class ChatsAdapter(var msgs:MutableList<ChatDataClass?>?):Adapter<ViewHolder>() {

    val myMessages:Int=100
    val otherMessages:Int=200
    val myImgMessages:Int=300
    val otherImgMessages:Int=400

    fun upDateMsgs(Msgs:ChatDataClass){
        msgs?.add(Msgs)
        notifyItemInserted(msgs!!.size)
    }

    class myChatsHolder(val msgBinding:SingleMymessagesDesignBinding): ViewHolder(msgBinding.root){
        fun bind(msg:ChatDataClass){
            msgBinding.myMessage=msg
            msgBinding.executePendingBindings()
        }
    }

    class otherChatHolder(val OtherChatBinding: SingleOthermessagesDesignBinding):ViewHolder(OtherChatBinding.root){
        fun bind(messag: ChatDataClass){
            OtherChatBinding.otherMsgs=messag
            OtherChatBinding.executePendingBindings()
        }
    }

    class myImgMessage(val imgMsgBinding:SingleMyimgsDesignBinding):ViewHolder(imgMsgBinding.root){
        fun bind(messag: ChatDataClass){
            imgMsgBinding.vm=messag
            imgMsgBinding.executePendingBindings()
        }
    }
    class otherImgMessage(val otherImgMsgBinding:SingleOtherimgsDesignBinding):ViewHolder(otherImgMsgBinding.root){
        fun bind(messag: ChatDataClass){
            otherImgMsgBinding.vm=messag
            otherImgMsgBinding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message=msgs?.get(position)
        if (message?.sendImg == true && message.imgUri !=null && message?.senderID == UserProvider.user?.userID){
            return myImgMessages
        }
        if (message?.sendImg == true && message.imgUri !=null && message?.senderID != UserProvider.user?.userID){
            return otherImgMessages
        }
        if(message?.senderID == UserProvider.user?.userID){
            return  myMessages
        }
        else{
            return otherMessages
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType ==100){
            val msgBinding=SingleMymessagesDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return myChatsHolder(msgBinding)
        }
        else if (viewType==200){
            val msgBinding=SingleOthermessagesDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return otherChatHolder(msgBinding)
        }
        else if(viewType ==300){
            val msgBinding=SingleMyimgsDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return myImgMessage(msgBinding)
        }else{
            val msgBinding=SingleOtherimgsDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return otherImgMessage(msgBinding)
        }
    }

    override fun getItemCount(): Int {
        return msgs?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is myChatsHolder){
            holder.bind(msgs?.get(position)!!)
        }
        if (holder is otherChatHolder){
            holder.bind(msgs?.get(position)!!)
        }
        if(holder is myImgMessage){
            holder.bind(msgs?.get(position)!!)
        }
        if (holder is otherImgMessage){
            holder.bind(msgs?.get(position)!!)
        }
    }

}