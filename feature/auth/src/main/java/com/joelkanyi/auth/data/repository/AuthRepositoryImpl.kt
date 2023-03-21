/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.auth.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.joelkanyi.auth.domain.repository.AuthRepository
import com.kanyideveloper.core.util.Resource
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        name: String
    ): Resource<AuthResult> {
        return try {
            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            response.user?.updateProfile(profileUpdates)?.await()
            firebaseAuth.currentUser?.sendEmailVerification()?.await()

            Resource.Success(response)
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "Unknown error occurred")
        }
    }

    override suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return try {
            val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (response.user?.isEmailVerified == true) {
                Resource.Success(response)
            } else {
                Resource.Error("Please verify your email address")
            }
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "Unknown error occurred")
        }
    }

    override suspend fun forgotPassword(email: String): Resource<Any> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success("Success")
        } catch (e: Exception) {
            return Resource.Error(e.localizedMessage ?: "Unknown error occurred")
        }
    }

    override suspend fun userAutologin(): Boolean {
        val uid = firebaseAuth.currentUser?.uid
        return uid != null
    }
}
