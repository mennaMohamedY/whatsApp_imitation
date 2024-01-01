package com.example.chat.chatroom

import com.google.firebase.firestore.ListenerRegistration

interface ChatNavigator {
    fun showToast(msg:String)
    fun showDialog(msg:String)
    fun updatemsg(msg:ChatDataClass)
    fun listener(listen:ListenerRegistration)
    fun scrollToBtn()
}