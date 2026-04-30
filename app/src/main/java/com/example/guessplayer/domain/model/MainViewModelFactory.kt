package com.example.guessplayer.domain.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guessplayer.domain.repository.PlayerRepository
import com.example.guessplayer.domain.repository.ProgressRepository

class MainViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val playerRepository: PlayerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(progressRepository, playerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
