package com.kanyideveloper.core_network.model

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestDto(
    @SerializedName("token")
    val token: String
)
