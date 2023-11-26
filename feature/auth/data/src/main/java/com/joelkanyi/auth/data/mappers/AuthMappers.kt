package com.joelkanyi.auth.data.mappers

import com.joelkanyi.auth.domain.entity.AuthResult
import com.joelkanyi.auth.domain.entity.User
import com.joelkanyi.network.model.AuthResponseDto

fun com.joelkanyi.network.model.AuthResponseDto.toDomain() = AuthResult(
    accessToken = token,
    user = user.toDomain()
)

fun com.joelkanyi.network.model.AuthResponseDto.UserDto.toDomain() = User(
    id = userId,
    firstName = firstName,
    lastName = lastName,
    email = email
)