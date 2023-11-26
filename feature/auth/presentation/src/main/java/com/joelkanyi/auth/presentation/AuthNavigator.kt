package com.joelkanyi.auth.presentation

interface AuthNavigator {
    fun openForgotPassword()
    fun openSignUp()
    fun openSignIn()
    fun switchNavGraphRoot()
    fun popBackStack()
}
