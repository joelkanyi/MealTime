package com.joelkanyi.network.utils

import com.joelkanyi.common.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T,
): Resource<T> {
    return withContext(dispatcher) {
        try {
            Resource.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            when (throwable) {
                is IOException -> {
                    Resource.Error(
                        message = "Please check your internet connection and try again later",
                        throwable = throwable,
                    )
                }

                is HttpException -> {
                    Resource.Error(
                        message = "Unknown failure occurred, please try again later",
                        throwable = throwable,
                    )
                }

                else -> {
                    Resource.Error(
                        message = "Unknown failure occurred, please try again later",
                        throwable = throwable,
                    )
                }
            }
        }
    }
}