package com.example.chat.login

interface LoginNavigator {
    fun showLogs(msg:String)
    fun goToMain()
    fun showError(msg:String)
    fun navigateToSignIn()
}