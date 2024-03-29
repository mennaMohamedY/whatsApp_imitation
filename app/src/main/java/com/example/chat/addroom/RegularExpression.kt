package com.example.chat.addroom

import java.util.regex.Pattern

val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

public fun checkEmail(email: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
}

val Phone_Number_Pattern:Pattern=Pattern.compile(
            "20"+
            "[1-9]{10}"
)
public fun checkPhoneNumber(phonenum:String):Boolean{
    return Phone_Number_Pattern.matcher(phonenum).matches()
}
