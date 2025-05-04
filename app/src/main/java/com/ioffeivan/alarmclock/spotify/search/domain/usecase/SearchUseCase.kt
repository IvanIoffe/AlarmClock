package com.ioffeivan.alarmclock.spotify.search.domain.usecase

import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(searchRequest: SearchRequest) = searchRepository.search(searchRequest)
}