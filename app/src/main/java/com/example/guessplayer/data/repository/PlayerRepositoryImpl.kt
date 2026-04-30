package com.example.guessplayer.data.repository

import com.example.guessplayer.data.source.PlayerLocalDataSource
import com.example.guessplayer.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val dataSource: PlayerLocalDataSource
) : PlayerRepository {

    override fun getPlayers() = dataSource.getNames("players.txt")

    override fun getClubs() = dataSource.getClubs("clubs.txt")

    override fun getYears() = dataSource.getYears("years.txt")
}
