package com.example.chat.models

import android.net.Uri
import com.example.chat.chatroom.ChatDataClass
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.v
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class FireStoreUtiles {
    val UsersCollectionName="Users"
    val MessagesCollectionName="Messages"
    val ChatsCollectionReference="Chats"
    val MsgsCollectionName="Messages"

    fun getCollectionRef(collectionName: String):CollectionReference{
        val database=FirebaseFirestore.getInstance()
        val collRef=database.collection(collectionName)
        return collRef
    }

    fun InsertUserToDB(user:User): Task<Void> {
        //hna b3ml row gwa collection asmha users awl haga lw
        //al collection msh mwgouda hyrouh y3mlha create
        // tany haga lw al row da msh mwgoud hyrouh y3mlo create
        val docRef=getCollectionRef(UsersCollectionName).document(user.userID!!)
        return docRef.set(user)
    }

    //when we start new chat with any user
    // in the users collection on each user document there will be a messages collection which contains multiple usersid document
    //and inside each user there will be the messages
    fun insertChatToUsers(senderid: String, getter_uid:String, msgg:ChatDataClass):Task<Void>{
        //frdn ana dlw2ty aly 3mlt sign in w hb3t msg laya fana flnext step kda shwrt 3ala asmy gwa kol alusers
        val docRef=getCollectionRef(UsersCollectionName).document(senderid)

        //hna 3mlt gwah collection gwaha document bl id bta3 aya
        var getterRef = docRef.collection(getter_uid)

        //hna ana bshawr 3ala aya w hhot gwah almsgs aly hb3thalha
        var msgs=getterRef.document()
        msgg.messageID=msgs.id
        return msgs.set(msgg)
    }
    fun insertChatToUsersTry2(senderid: String, getter_uid:String, user:User):Task<Void> {
        //frdn ana dlw2ty aly 3mlt sign in w hb3t msg laya fana flnext step kda shwrt 3ala asmy gwa kol alusers
        val docRef = getCollectionRef(UsersCollectionName).document(senderid)

        //hna 3mlt gwah collection gwaha document bl id bta3 aya
        var chatsRef = docRef.collection(ChatsCollectionReference)
        val chatsDocs = chatsRef.document(getter_uid)
        return chatsDocs.set(user)
    }

    //******** this one is currently used to insert new user to chats collection also to insert the chat messages in the users documents
    fun insertMsgToChat(senderid: String, getter_uid:String,msg:ChatDataClass,user:User):Task<Void> {
        //frdn ana dlw2ty aly 3mlt sign in w hb3t msg laya fana flnext step kda shwrt 3ala asmy gwa kol alusers
        val docRef1 = getCollectionRef(UsersCollectionName).document(senderid)
        val docRef2=docRef1.collection(ChatsCollectionReference).document(getter_uid)
        val chatsRef=docRef2.set(user)
        val msgRef=docRef2.collection(MsgsCollectionName).document()
        msg.messageID=msgRef.id
        return msgRef.set(msg)
    }



    fun InsertMsgToDB2(msg:ChatDataClass):Task<Void>{
        //rouh a3ml collection asmha Messages lw msh mwgouda
        //w hot gwaha document "row" bnfs almsg id
        val docRef=getCollectionRef(MessagesCollectionName).document()
        msg.messageID=docRef.id
        return docRef.set(msg)
    }

    //Note: Here we Use it if we want to Get Colllection From document Bashmohnds Abdalla's Help
    fun getAllChat(senderid: String):Task<QuerySnapshot>{
        val chatRef=getCollectionRef(UsersCollectionName).document(senderid)
        val chatCollectionRef = chatRef.collection(chatRef.id)
        return chatCollectionRef.get()
    }
    fun getAllChatzz(senderid: String):Task<QuerySnapshot>{
        val chatRef=getCollectionRef(UsersCollectionName).document(senderid).collection(ChatsCollectionReference)
        return chatRef.get()
    }
    fun getAllChatzzHomeFrag(senderid: String):CollectionReference{
        val chatRef=getCollectionRef(UsersCollectionName).document(senderid).collection(ChatsCollectionReference)
        return chatRef
    }


    fun getAllMsgs(getter_uid :  String,senderid: String):Query{
//        val chatRef=getCollectionRef(UsersCollectionName).document(getter_uid).collection(MessagesCollectionName)
//        return chatRef.get()

        val chatREf=getCollectionRef(UsersCollectionName).document(senderid).collection(ChatsCollectionReference)
            .document(getter_uid).collection(MsgsCollectionName).orderBy("tm",Query.Direction.ASCENDING)
        return chatREf
    }

    fun updatelastMessage(senderID:String,getter_uid: String,lastMsg:String){
        val userr=getCollectionRef(UsersCollectionName).document(senderID).collection(ChatsCollectionReference)
            .document(getter_uid)
            .update(mapOf("lastMsg" to lastMsg))
    }


    fun GetUserFromDB(uid:String):Task<DocumentSnapshot>{
        //hna b2olo gwa al collection aly asmha users roh hatly alrow aly leh al id da
        val docRef=getCollectionRef(UsersCollectionName).document(uid)
        return docRef.get()
    }

    fun InsertMsgToDB(msg:ChatDataClass):Task<Void>{
        //rouh a3ml collection asmha Messages lw msh mwgouda
        //w hot gwaha document "row" bnfs almsg id
        val docRef=getCollectionRef(MessagesCollectionName).document()
        msg.messageID=docRef.id
        return docRef.set(msg)
    }

    fun updateUser(uid:String,profilePicUpdated:Boolean,imgUri: Uri?){
        val userr=getCollectionRef(UsersCollectionName).document(uid)
            .update(mapOf("userProfilePic" to profilePicUpdated,"userimgUri" to imgUri))
    }

    fun getAllChatsInUser(uid: String):Task<DocumentSnapshot>{
        //var tasksRef=getCollectionRef(UsersCollectionName).document(uid)
        val v=getCollectionRef(UsersCollectionName).document(uid).get()
        return v
    }

    fun getAllChat():CollectionReference{
        var tasksRef=getCollectionRef(MessagesCollectionName)
        return tasksRef
    }

    fun getAllUsers():CollectionReference{
        val usersRef=getCollectionRef(UsersCollectionName)
        return usersRef
    }

    fun gettALlUsers():Task<QuerySnapshot>{
        val usersRef=getCollectionRef(UsersCollectionName).get()
        return usersRef
    }


}