package com.joelkanyi.auth.domain.entity

data class AuthResult(
    val accessToken: String,
    val user: User
)
