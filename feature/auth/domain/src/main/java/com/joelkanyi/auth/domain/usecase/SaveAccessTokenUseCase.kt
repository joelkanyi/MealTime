package com.joelkanyi.auth.domain.usecase

import com.joelkanyi.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SaveAccessTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(accessToken: String) = authRepository.saveAccessToken(accessToken)
}