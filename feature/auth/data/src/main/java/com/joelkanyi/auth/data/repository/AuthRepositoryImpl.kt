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

import com.joelkanyi.auth.data.mappers.toDomain
import com.joelkanyi.auth.domain.entity.AuthResult
import com.joelkanyi.auth.domain.repository.AuthRepository
import com.joelkanyi.common.util.Resource
import com.joelkanyi.common.util.safeApiCall
import com.joelkanyi.settings.domain.MealtimeSettings
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val mealDbApi: com.joelkanyi.network.MealDbApi,
    private val mealtimeSettings: MealtimeSettings,
) : AuthRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        name: String
    ): Resource<AuthResult> {
        return safeApiCall(Dispatchers.IO) {
            mealDbApi.registerUser(
                com.joelkanyi.network.model.RegisterRequestDto(
                    email = email,
                    password = password,
                    name = name
                )
            ).toDomain()
        }
    }

    override suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return safeApiCall(Dispatchers.IO) {
            mealDbApi.loginUser(
                com.joelkanyi.network.model.LoginRequestDto(
                    email = email,
                    password = password
                )
            ).toDomain()
        }
    }

    override suspend fun forgotPassword(email: String): Resource<Any> {
        return safeApiCall(Dispatchers.IO) {
            mealDbApi.forgotPassword(
                email = email
            )
        }
    }

    override suspend fun logoutUser() {
        mealtimeSettings.clear()
    }

    override suspend fun signInWithGoogle(idToken: String): Resource<AuthResult> {
        return safeApiCall(Dispatchers.IO) {
            mealDbApi.signInWithGoogle(
                idToken = idToken
            ).toDomain()
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        mealtimeSettings.saveAccessToken(accessToken)
    }

    override suspend fun saveUserId(userId: String) {
        mealtimeSettings.saveUserId(userId)
    }
}
