package com.example.guessplayer.domain.repository

interface PlayerRepository {
    fun getPlayers(): List<String>
    fun getClubs(): List<List<String>>
    fun getYears(): List<List<String>>
}
