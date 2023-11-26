package com.joelkanyi.auth.domain.usecase

import com.joelkanyi.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase  @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) = authRepository.forgotPassword(email)
}