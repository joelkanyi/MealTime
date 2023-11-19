package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.FavoritesRepository
import javax.inject.Inject

class DeleteAllFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke() = favoritesRepository.deleteAllFavorites()
}
