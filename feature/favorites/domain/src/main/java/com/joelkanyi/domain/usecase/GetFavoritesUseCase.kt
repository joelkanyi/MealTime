package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.FavoritesRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val favoritesRepository: com.joelkanyi.domain.repository.FavoritesRepository
) {
    operator fun invoke() = favoritesRepository.getFavorites()
}