package com.example.guessplayer.presentation.main

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.core.view.isVisible
import com.example.guessplayer.databinding.ActivityMainBinding

class OverlayTouchHandler(
    private val binding: ActivityMainBinding,
    private val onHideOverlay: () -> Unit
) {
    @SuppressLint("ClickableViewAccessibility")
    fun setupTouchListener() {
        binding.overlayWindows.overlayWindow.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()

                val visibleCards = listOf(
                    binding.overlayWindows.settingsCardView to binding.overlayWindows.settingsCardView.id,
                    binding.overlayWindows.trophyCardView to binding.overlayWindows.trophyCardView.id,
                    binding.overlayWindows.askCardView to binding.overlayWindows.askCardView.id,
                    binding.overlayWindows.newsCardView to binding.overlayWindows.newsCardView.id
                ).filter { it.first.isVisible }

                val isInsideAnyWindow = visibleCards.any { (cardView, _) ->
                    val location = IntArray(2)
                    cardView.getLocationOnScreen(location)
                    val viewX = location[0]
                    val viewY = location[1]

                    x >= viewX && x <= viewX + cardView.width &&
                            y >= viewY && y <= viewY + cardView.height
                }

                if (!isInsideAnyWindow) {
                    onHideOverlay()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}
