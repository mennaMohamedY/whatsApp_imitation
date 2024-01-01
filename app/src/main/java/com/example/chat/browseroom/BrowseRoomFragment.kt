package com.example.chat.browseroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chat.R
import com.example.chat.databinding.FragmentMyRoomsBinding
import com.example.chat.myroomsfragment.MyRoomsViewBinding


class BrowseRoomFragment : Fragment() {
    lateinit var myRoomsBinding: FragmentMyRoomsBinding
    lateinit var myRoomsViewModel:MyRoomsViewBinding


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

       // return inflater.inflate(R.layout.fragment_browse_room, container, false)
    }

}