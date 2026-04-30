package com.example.guessplayer.presentation.ui.animations.main

import android.view.View
import androidx.cardview.widget.CardView
import com.example.guessplayer.databinding.ActivityMainBinding
import androidx.core.view.isVisible

class OverlayAnimator(
    private val binding: ActivityMainBinding
) {

    companion object {
        private const val FADE_DURATION = 300L
    }

    fun showOverlayWindow() {
        binding.overlayWindows.overlayWindow.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(FADE_DURATION)
                .start()
        }
    }

    fun hideOverlayWindow(onComplete: (() -> Unit)? = null) {
        binding.overlayWindows.overlayWindow.animate()
            .alpha(0f)
            .setDuration(FADE_DURATION * 2)
            .withEndAction {
                binding.overlayWindows.overlayWindow.apply {
                    visibility = View.GONE
                    alpha = 1f
                }
                hideAllCards()
                onComplete?.invoke()
            }
            .start()
    }

    fun hideAllCards() {
        animateCardHide(binding.overlayWindows.settingsCardView)
        animateCardHide(binding.overlayWindows.trophyCardView)
        animateCardHide(binding.overlayWindows.askCardView)
        animateCardHide(binding.overlayWindows.newsCardView)
    }

    private fun animateCardHide(cardView: CardView) {
        if (cardView.isVisible) {
            cardView.animate()
                .alpha(0f)
                .setDuration(FADE_DURATION)
                .withEndAction {
                    cardView.visibility = View.GONE
                    cardView.alpha = 1f
                }
                .start()
        } else {
            cardView.visibility = View.GONE
            cardView.alpha = 1f
        }
    }

    fun showActiveCard(cardType: com.example.guessplayer.domain.model.CardType?) {
        hideAllCards()

        when (cardType) {
            com.example.guessplayer.domain.model.CardType.SETTINGS ->
                showCard(binding.overlayWindows.settingsCardView)
            com.example.guessplayer.domain.model.CardType.TROPHY ->
                showCard(binding.overlayWindows.trophyCardView)
            com.example.guessplayer.domain.model.CardType.ASK ->
                showCard(binding.overlayWindows.askCardView)
            com.example.guessplayer.domain.model.CardType.NEWS ->
                showCard(binding.overlayWindows.newsCardView)
            null -> {}
        }
    }

    private fun showCard(cardView: CardView) {
        cardView.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(FADE_DURATION)
                .start()
        }
    }

    fun isOverlayVisible(): Boolean {
        return binding.overlayWindows.overlayWindow.isVisible
    }
}
