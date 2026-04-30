package com.example.guessplayer.domain.repository

import com.example.guessplayer.domain.model.GameProgress

interface ProgressRepository {
    fun getProgress(): GameProgress
    fun updateProgress(progress: GameProgress)
    fun resetProgress()
}
