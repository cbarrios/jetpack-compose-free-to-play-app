package com.lalosapps.freetoplay.domain.usecases

import com.lalosapps.freetoplay.domain.model.GameDetails
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoritesFlowUseCase @Inject constructor(
    private val gamesRepository: GamesRepository
) {

    operator fun invoke(): Flow<List<GameDetails>> {
        return gamesRepository.getFavoritesFlow()
    }
}