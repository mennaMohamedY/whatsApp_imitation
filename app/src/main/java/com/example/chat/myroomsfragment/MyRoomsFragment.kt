package com.example.chat.myroomsfragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chat.R
import com.example.chat.chatroom.ChatRoomActivity
import com.example.chat.databinding.FragmentMyRoomsBinding


class MyRoomsFragment : Fragment() {
    lateinit var myRoomsViewModel: MyRoomsViewBinding
    lateinit var myRoomsBinding: FragmentMyRoomsBinding
    lateinit var Room_adapter:MyRoomsAdapter
    var RoomArrayData :ArrayList<RoomDataClass?>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myRoomsViewModel=ViewModelProvider(this).get(MyRoomsViewBinding::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myRoomsBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_my_rooms,container,false)
        return myRoomsBinding.root
        //return inflater.inflate(R.layout.fragment_my_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArrayFillment()
        Room_adapter= MyRoomsAdapter(RoomArrayData)
        Room_adapter.updateData(RoomArrayData)
        myRoomsBinding.roomsRecyclerview.adapter=Room_adapter
        Room_adapter.onRoomClickListener=object:MyRoomsAdapter.OnRoomClickListener{
            override fun OnRoomClicked(item: RoomDataClass, position: Int) {
                val intent = Intent(requireContext(),ChatRoomActivity::class.java)
                startActivity(intent)
            }
        }




    }
    fun ArrayFillment(){
        RoomArrayData= ArrayList()
        RoomArrayData?.add(RoomDataClass(R.drawable.movies,"The Movies \n   zone","13 Members"))
        RoomArrayData?.add(RoomDataClass(R.drawable.music,"The Music \n   zone","11 Members"))
        RoomArrayData?.add(RoomDataClass(R.drawable.sports,"The Sports \n   zone","19 Members"))
        RoomArrayData?.add(RoomDataClass(R.drawable.movies,"The Movies \n   zone","12 Members"))
        RoomArrayData?.add(RoomDataClass(R.drawable.music,"The Movies \n   zone","4 Members"))
        RoomArrayData?.add(RoomDataClass(R.drawable.sports,"The Sports \n   zone","19 Members"))
        RoomArrayData?.add(RoomDataClass(R.drawable.sports,"The Sports \n   zone","19 Members"))

    }

}