package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.FavoritesRepository
import javax.inject.Inject

class DeleteAFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(id: String) = favoritesRepository.deleteAFavorite(id)
}