package com.joelkanyi.auth.domain.usecase

import com.joelkanyi.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCase  @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = authRepository.loginUser(email, password)
}