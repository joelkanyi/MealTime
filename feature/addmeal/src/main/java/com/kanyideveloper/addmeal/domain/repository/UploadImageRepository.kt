package com.kanyideveloper.addmeal.domain.repository

import android.net.Uri
import com.kanyideveloper.core.util.Resource

interface UploadImageRepository {
    suspend fun uploadImage(imageUri: Uri): Resource<String>
}