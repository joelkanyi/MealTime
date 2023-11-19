package com.kanyideveloper.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val comment: String,
    val id: Int,
    val rating: Int,
    val user: User
): Parcelable
