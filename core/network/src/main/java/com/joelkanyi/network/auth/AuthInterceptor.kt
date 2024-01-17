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
package com.joelkanyi.network.auth

import com.joelkanyi.network.Constants
import com.joelkanyi.network.MealtimeApiService
import com.joelkanyi.network.model.RefreshTokenRequestDto
import com.joelkanyi.settings.domain.MealtimePreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(
    private val mealtimePreferences: MealtimePreferences,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val savedToken = runBlocking {
            mealtimePreferences.getAccessToken().first()
        }

        if (savedToken?.isNotEmpty() == true) {
            val response = makeRequest(
                chain = chain,
                token = savedToken
            )

            return if (response.code == 401) {
                runBlocking {
                    // Close the response body
                    response.close()

                    // Refresh token
                    val generatedToken = refreshExpiredToken(savedToken)

                    // Save the new token
                    saveNewToken(generatedToken)

                    // Make the request again with the new token
                    val newToken = mealtimePreferences.getAccessToken().first()
                    makeRequest(
                        chain = chain,
                        token = newToken
                    )
                }
            } else {
                response
            }
        } else {
            return makeRequest(
                chain = chain,
                token = null
            )
        }
    }

    private fun makeRequest(
        chain: Interceptor.Chain,
        token: String?
    ): Response {
        val newChain = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newChain)
    }

    private suspend fun saveNewToken(generatedToken: String?) {
        if (generatedToken != null) {
            mealtimePreferences.saveAccessToken(generatedToken)
        }
    }

    private suspend fun refreshExpiredToken(savedToken: String): String? {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val mealtimeApiService = retrofit.create(MealtimeApiService::class.java)

        return mealtimeApiService.refreshToken(
            RefreshTokenRequestDto(savedToken)
        )?.token
    }
}
