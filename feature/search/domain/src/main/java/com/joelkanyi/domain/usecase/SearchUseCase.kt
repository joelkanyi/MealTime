package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        searchOption: String,
        searchParam: String
    ) = searchRepository.search(
        searchOption,
        searchParam
    )
}