package com.kanyideveloper.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val id: Int,
    val name: String,
    val quantity: String?
) : Parcelable
