package com.kanyideveloper.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String
) : Parcelable
