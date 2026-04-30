package com.example.guessplayer.presentation.ui.animations.main

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.example.guessplayer.databinding.ActivityMainBinding

class MainMenuAnimator(
    private val binding: ActivityMainBinding
) {

    companion object {
        private const val ROTATION_DURATION = 300L
        private const val CONTAINER_FADE_DURATION = 180L
        private const val BUTTON_APPEAR_DURATION = 400L
        private const val BUTTON_DISAPPEAR_DURATION = 300L
        private const val BUTTON_APPEAR_DELAY = 100L
        private const val BUTTON_DISAPPEAR_DELAY = 50L
        private const val CONTAINER_HIDE_DELAY = 150L
        private const val BUTTON_TRANSLATION_OFFSET = 100f
    }

    private var isExpanded = false

    fun setupInitialState() {
        binding.hiddenButtonsContainer.hiddenButtonsContainer.apply {
            alpha = 0f
            visibility = View.GONE
        }

        listOf(
            binding.hiddenButtonsContainer.buttonSettings,
            binding.hiddenButtonsContainer.buttonTrophyCollection,
            binding.hiddenButtonsContainer.buttonAsk,
            binding.hiddenButtonsContainer.buttonNews
        ).forEach { button ->
            button.apply {
                translationY = BUTTON_TRANSLATION_OFFSET
                alpha = 0f
            }
        }
    }

    fun expandMenu(onComplete: (() -> Unit)? = null) {
        if (isExpanded) {
            onComplete?.invoke()
            return
        }

        isExpanded = true

        ObjectAnimator.ofFloat(
            binding.buttonSupport,
            View.ROTATION,
            0f,
            180f
        ).apply {
            duration = ROTATION_DURATION
            start()
        }

        binding.hiddenButtonsContainer.hiddenButtonsContainer.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(CONTAINER_FADE_DURATION)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    val buttons = listOf(
                        binding.hiddenButtonsContainer.buttonSettings,
                        binding.hiddenButtonsContainer.buttonTrophyCollection,
                        binding.hiddenButtonsContainer.buttonAsk,
                        binding.hiddenButtonsContainer.buttonNews
                    )

                    buttons.forEachIndexed { index, button ->
                        button.postDelayed({
                            if (isExpanded) {
                                button.animate()
                                    .translationY(0f)
                                    .alpha(1f)
                                    .setDuration(BUTTON_APPEAR_DURATION)
                                    .setInterpolator(OvershootInterpolator())
                                    .withEndAction {
                                        if (index == buttons.lastIndex) {
                                            onComplete?.invoke()
                                        }
                                    }
                                    .start()
                            }
                        }, (index * BUTTON_APPEAR_DELAY))
                    }
                }
                .start()
        }
    }

    fun collapseMenu(onComplete: (() -> Unit)? = null) {
        if (!isExpanded) {
            onComplete?.invoke()
            return
        }

        isExpanded = false

        ObjectAnimator.ofFloat(
            binding.buttonSupport,
            View.ROTATION,
            180f,
            0f
        ).apply {
            duration = ROTATION_DURATION
            start()
        }

        val buttons = listOf(
            binding.hiddenButtonsContainer.buttonSettings,
            binding.hiddenButtonsContainer.buttonTrophyCollection,
            binding.hiddenButtonsContainer.buttonAsk,
            binding.hiddenButtonsContainer.buttonNews
        )

        buttons.reversed().forEachIndexed { index, button ->
            button.postDelayed({
                if (!isExpanded) {
                    button.animate()
                        .translationY(BUTTON_TRANSLATION_OFFSET)
                        .alpha(0f)
                        .setDuration(BUTTON_DISAPPEAR_DURATION)
                        .setInterpolator(AccelerateInterpolator())
                        .start()
                }
            }, (index * BUTTON_DISAPPEAR_DELAY))
        }

        binding.hiddenButtonsContainer.hiddenButtonsContainer.postDelayed({
            if (!isExpanded) {
                binding.hiddenButtonsContainer.hiddenButtonsContainer.animate()
                    .alpha(0f)
                    .setDuration(BUTTON_DISAPPEAR_DURATION)
                    .setInterpolator(AccelerateInterpolator())
                    .withEndAction {
                        binding.hiddenButtonsContainer.hiddenButtonsContainer.visibility = View.GONE
                        onComplete?.invoke()
                    }
                    .start()
            }
        }, CONTAINER_HIDE_DELAY)
    }

    fun toggleMenu() {
        if (isExpanded) {
            collapseMenu()
        } else {
            expandMenu()
        }
    }

    fun isMenuExpanded(): Boolean = isExpanded
}