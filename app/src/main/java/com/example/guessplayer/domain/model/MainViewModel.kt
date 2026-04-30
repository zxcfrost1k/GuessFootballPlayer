package com.example.guessplayer.domain.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guessplayer.domain.repository.PlayerRepository
import com.example.guessplayer.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("UNUSED_PARAMETER")
class MainViewModel(
    private val progressRepository: ProgressRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    // Состояние оверлея
    private val _isOverlayVisible = MutableStateFlow(false)
    val isOverlayVisible = _isOverlayVisible.asStateFlow()

    private val _activeCard = MutableStateFlow<CardType?>(null)
    val activeCard = _activeCard.asStateFlow()

    private val _isMenuExpanded = MutableStateFlow(false)
    val isMenuExpanded: StateFlow<Boolean> = _isMenuExpanded.asStateFlow()

    // Прогресс игрока
    private val _gameProgress = MutableStateFlow<GameProgress?>(null)

    private val _balance = MutableStateFlow(0)
    val balance: StateFlow<Int> = _balance.asStateFlow()

    private val _userLevel = MutableStateFlow(0)
    val userLevel: StateFlow<Int> = _userLevel.asStateFlow()

    // Старт главы
    private val _chapters = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val chapters: StateFlow<Map<Int, Int>> = _chapters.asStateFlow()

    private val _startChapterEvent = MutableSharedFlow<Int>()
    val startChapterEvent = _startChapterEvent.asSharedFlow()

    // Константы
    companion object {
        const val MAX_LEVEL_CHAPTER_1 = 13
        const val MAX_LEVEL_CHAPTER_2 = 0
        const val MAX_LEVEL_CHAPTER_3 = 0
        const val MAX_LEVEL_CHAPTER_4 = 0
        const val MAX_LEVEL_CHAPTER_5 = 0
    }

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch {
            val progress = progressRepository.getProgress()
            _gameProgress.value = progress

            _balance.value = progress.balance
            _userLevel.value = progress.userLevel
            _chapters.value = progress.chapters
        }
    }

    fun getChapterLevel(chapterNumber: Int): Int {
        return _gameProgress.value?.chapters?.get(chapterNumber) ?: 0
    }

    fun getMaxLevelForChapter(chapter: Int): Int {
        return when (chapter) {
            1 -> MAX_LEVEL_CHAPTER_1
            2 -> MAX_LEVEL_CHAPTER_2
            3 -> MAX_LEVEL_CHAPTER_3
            4 -> MAX_LEVEL_CHAPTER_4
            5 -> MAX_LEVEL_CHAPTER_5
            else -> 0
        }
    }

    fun canStartChapter(chapterNumber: Int): Boolean {
        val currentLevel = _gameProgress.value?.chapters?.get(chapterNumber) ?: 0
        val maxLevel = getMaxLevelForChapter(chapterNumber)
        return currentLevel < maxLevel
    }

    fun resetProgress(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            progressRepository.resetProgress()
            loadProgress()
            onComplete()
        }
    }

    fun onChapterSelected(chapter: Int) {
        viewModelScope.launch {
            _startChapterEvent.emit(chapter)
        }
    }

    fun showCard(type: CardType) {
        _activeCard.value = type
        _isOverlayVisible.value = true
    }

    fun hideOverlay() {
        _isOverlayVisible.value = false
        _activeCard.value = null
    }

    fun toggleSupportMenu() {
        _isMenuExpanded.value = !_isMenuExpanded.value
    }

    fun collapseMenu() {
        _isMenuExpanded.value = false
    }
}

enum class CardType { SETTINGS, TROPHY, ASK, NEWS }
