package com.example.guessplayer.domain.usecase

import com.example.guessplayer.domain.repository.PlayerRepository

class GetPlayerInfoUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Triple<
            List<String>,
            List<List<String>>,
            List<List<String>>
            > {
        return Triple(
            repository.getPlayers(),
            repository.getClubs(),
            repository.getYears()
        )
    }
}
