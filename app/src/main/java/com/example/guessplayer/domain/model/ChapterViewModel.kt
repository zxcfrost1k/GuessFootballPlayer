package com.example.guessplayer.domain.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChapterViewModel : ViewModel() {

    val balance = MutableLiveData<Int>()
    val level = MutableLiveData<Int>()

    val isSettingsVisible = MutableLiveData(false)
    val isTrophyVisible = MutableLiveData(false)
    val isAskVisible = MutableLiveData(false)
    val isNewsVisible = MutableLiveData(false)

    fun toggleSettings() {
        isSettingsVisible.value = !(isSettingsVisible.value ?: false)
    }

    fun resetProgress() {
        balance.value = 0
        level.value = 1
    }
}
