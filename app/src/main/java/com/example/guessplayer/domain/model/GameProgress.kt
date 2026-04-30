package com.example.guessplayer.domain.model

data class GameProgress(
    val chapters: Map<Int, Int>,
    val balance: Int,
    val userLevel: Int,
    val selection: Int
)
