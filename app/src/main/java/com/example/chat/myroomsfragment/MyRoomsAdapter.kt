package com.example.chat.myroomsfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chat.R
import com.example.chat.databinding.SingleRoomDesignBinding

class MyRoomsAdapter(var Dataclass:ArrayList<RoomDataClass?>?) :Adapter<MyRoomsAdapter.myRoomHolder>(){

    var onRoomClickListener:OnRoomClickListener?=null



    fun updateData(dataclass: ArrayList<RoomDataClass?>?){
        Dataclass=dataclass
        notifyDataSetChanged()
    }

    class myRoomHolder(val singleRoomDesign:SingleRoomDesignBinding) :ViewHolder(singleRoomDesign.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myRoomHolder {
        val singleRoomDesign=SingleRoomDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return myRoomHolder(singleRoomDesign)
    }

    override fun onBindViewHolder(holder: myRoomHolder, position: Int) {
        var currentItem=Dataclass?.get(position)
        //holder.singleRoomDesign.icon.setImageResource(cu)
        with(holder){
            with(singleRoomDesign){
                icon.setImageResource(currentItem!!.roomImg)
                roomname.text=currentItem.roomName
                roommembers.text=currentItem.roomMembers
            }
        }
        holder.singleRoomDesign.root.setOnClickListener({
            onRoomClickListener?.OnRoomClicked(currentItem!!,position)
        })
    }

    interface OnRoomClickListener{
        fun OnRoomClicked(item:RoomDataClass,position: Int)
    }

    override fun getItemCount(): Int {
       return 5
        //Dataclass?.size?:5
    }
}