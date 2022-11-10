package com.lalosapps.freetoplay.domain.usecases

import com.lalosapps.freetoplay.domain.model.Game
import com.lalosapps.freetoplay.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGamesFlowUseCase @Inject constructor(
    private val gamesRepository: GamesRepository
) {

    operator fun invoke(): Flow<List<Game>> {
        return gamesRepository.getGamesFlow()
    }
}