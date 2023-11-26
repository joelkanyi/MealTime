package com.joelkanyi.auth.domain.usecase

import com.joelkanyi.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase  @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = authRepository.signInWithGoogle(idToken)
}