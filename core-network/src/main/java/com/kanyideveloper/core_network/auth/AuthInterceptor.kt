package com.kanyideveloper.core_network.auth

import com.kanyideveloper.core_network.Constants
import com.kanyideveloper.core_network.MealDbApi
import com.kanyideveloper.core_network.model.RefreshTokenRequestDto
import com.kanyideveloper.preferences.domain.MealtimeSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class AuthInterceptor(
    private val mealtimeSettings: MealtimeSettings,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val token = runBlocking {
            mealtimeSettings.getAccessToken().first()
        }

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()

        val response = chain.proceed(request)

        if (response.code == 500) {
            Timber.e("AuthInterceptor: intercept: Unauthorized")
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val mealDbApi = retrofit.create(MealDbApi::class.java)

            val refreshTokenRequestDto = RefreshTokenRequestDto(
                token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2Vsa2FueWk5OEBnbWFpbC5jb20iLCJpYXQiOjE3MDAyOTE0MjUsImV4cCI6MTcwMDM3NzgyNX0.1fdDm8QMVxxvH54GmPSwYIf4dqMg31TqKom_Jbl2WEQ"
            )

            val refreshTokenResponse = runBlocking {
                mealDbApi.refreshToken(refreshTokenRequestDto)
            }

            Timber.e("AuthInterceptor: intercept: refreshTokenResponse: $refreshTokenResponse")

            if (refreshTokenResponse.isSuccessful) {
                val newToken = refreshTokenResponse.body()?.token
                Timber.d("AuthInterceptor: intercept: newToken: $newToken")
                if (newToken != null) {
                    runBlocking {
                        mealtimeSettings.saveAccessToken(newToken)
                    }
                }
            }
        }

        return response
    }
}