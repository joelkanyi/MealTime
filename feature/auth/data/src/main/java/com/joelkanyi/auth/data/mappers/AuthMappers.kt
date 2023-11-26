package com.joelkanyi.auth.data.mappers

import com.joelkanyi.auth.domain.entity.AuthResult
import com.joelkanyi.auth.domain.entity.User
import com.kanyideveloper.core_network.model.AuthResponseDto

fun AuthResponseDto.toDomain() = AuthResult(
    accessToken = token,
    user = user.toDomain()
)

fun AuthResponseDto.UserDto.toDomain() = User(
    id = userId,
    firstName = firstName,
    lastName = lastName,
    email = email
)