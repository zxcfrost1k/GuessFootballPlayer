package com.example.guessplayer.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.guessplayer.ChapterDefaultActivityRECAST
import com.example.guessplayer.R
import com.example.guessplayer.data.repository.PlayerRepositoryImpl
import com.example.guessplayer.data.repository.ProgressRepositoryImpl
import com.example.guessplayer.data.source.PlayerLocalDataSource
import com.example.guessplayer.data.source.ProgressLocalDataSource
import com.example.guessplayer.databinding.ActivityMainBinding
import com.example.guessplayer.domain.model.CardType
import com.example.guessplayer.domain.model.MainViewModel
import com.example.guessplayer.domain.model.MainViewModelFactory
import com.example.guessplayer.presentation.chapter.FragmentForChapter1
import com.example.guessplayer.presentation.chapter.FragmentForChapter2
import com.example.guessplayer.presentation.chapter.FragmentForChapter3
import com.example.guessplayer.presentation.chapter.FragmentForChapter4
import com.example.guessplayer.presentation.chapter.FragmentForChapter5
import com.example.guessplayer.presentation.ui.pager.ChaptersPagerAdapter
import com.example.guessplayer.presentation.ui.animations.main.MainMenuAnimator
import com.example.guessplayer.presentation.ui.animations.main.OverlayAnimator
import com.example.guessplayer.presentation.ui.navigation.NavigationBarManager
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity: AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navigationBarManager: NavigationBarManager
    private lateinit var viewModel: MainViewModel
    private lateinit var menuAnimator: MainMenuAnimator
    private lateinit var overlayAnimator: OverlayAnimator
    private lateinit var overlayTouchHandler: OverlayTouchHandler

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.loadProgress()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationBarManager = NavigationBarManager(window)
        navigationBarManager.hide()

        setupBinding()
        setupAnimators()
        setupViewModel()
        setupViewPager()
        setupObservers()
        setupClickListeners()
        setupTouchHandler()

        menuAnimator.setupInitialState()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupAnimators() {
        menuAnimator = MainMenuAnimator(binding)
        overlayAnimator = OverlayAnimator(binding)
    }

    private fun setupViewModel() {
        val progressDataSource = ProgressLocalDataSource(applicationContext)
        val progressRepository = ProgressRepositoryImpl(progressDataSource)

        val playerDataSource = PlayerLocalDataSource(applicationContext)
        val playerRepository = PlayerRepositoryImpl(playerDataSource)

        val factory = MainViewModelFactory(progressRepository, playerRepository)

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun setupViewPager() {
        val fragments = listOf(
            FragmentForChapter1(),
            FragmentForChapter2(),
            FragmentForChapter3(),
            FragmentForChapter4(),
            FragmentForChapter5()
        )

        val adapter = ChaptersPagerAdapter(
            fragments,
            supportFragmentManager,
            lifecycle
        )

        binding.pager.adapter = adapter
        binding.pager.setPageTransformer { page, position ->
            page.alpha = 1 - abs(position)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.balance.collect { balance ->
                binding.balanceLevelTextView.balanceTextView.text =
                    getString(R.string.balance_format, balance)
            }
        }

        lifecycleScope.launch {
            viewModel.userLevel.collect { level ->
                binding.balanceLevelTextView.userLevelTextView.text =
                    getString(R.string.user_level_format, level)
            }
        }

        lifecycleScope.launch {
            viewModel.chapters.collect { chapters ->
                updateChaptersUI(chapters)
            }
        }

        lifecycleScope.launch {
            viewModel.isOverlayVisible.collect { isVisible ->
                if (isVisible) {
                    overlayAnimator.showOverlayWindow()
                } else {
                    overlayAnimator.hideOverlayWindow()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.activeCard.collect { cardType ->
                overlayAnimator.showActiveCard(cardType)
            }
        }

        lifecycleScope.launch {
            viewModel.isMenuExpanded.collect { isExpanded ->
                if (isExpanded) {
                    menuAnimator.expandMenu()
                } else {
                    menuAnimator.collapseMenu()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.startChapterEvent.collect { chapter ->
                startChapter(chapter)
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonSupport.setOnClickListener {
            viewModel.toggleSupportMenu()
        }

        binding.hiddenButtonsContainer.buttonSettings.setOnClickListener {
            viewModel.showCard(CardType.SETTINGS)
        }

        binding.hiddenButtonsContainer.buttonTrophyCollection.setOnClickListener {
            viewModel.showCard(CardType.TROPHY)
        }

        binding.hiddenButtonsContainer.buttonAsk.setOnClickListener {
            viewModel.showCard(CardType.ASK)
        }

        binding.hiddenButtonsContainer.buttonNews.setOnClickListener {
            viewModel.showCard(CardType.NEWS)
        }

        binding.overlayWindows.buttonResetProgress.setOnClickListener {
            showResetDialog()
        }
    }

    private fun setupTouchHandler() {
        overlayTouchHandler = OverlayTouchHandler(binding) {
            viewModel.hideOverlay()
        }
        overlayTouchHandler.setupTouchListener()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun updateChaptersUI(chapters: Map<Int, Int>) { }

    private fun startChapter(chapter: Int) {
        if (overlayAnimator.isOverlayVisible()) return

        if (viewModel.canStartChapter(chapter)) {
            var delay = 0L

            if (menuAnimator.isMenuExpanded()) {
                delay = 200L
                viewModel.collapseMenu()
            }

            binding.buttonSupport.postDelayed({
                launchChapter(chapter)
            }, delay)
        } else {
            Toast.makeText(this, "Chapter $chapter is already finished",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchChapter(chapterNumber: Int) {
        val intent = android.content.Intent(this,
            ChapterDefaultActivityRECAST::class.java).apply {
            putExtra("currentChapter", chapterNumber)
            putExtra("currentLvl", viewModel.getChapterLevel(chapterNumber))
            putExtra("filenameForFootballPlayersClubs", "clubs_chapter_$chapterNumber.txt")
            putExtra("filenameForFootballPlayersTransferYears", "years_chapter_$chapterNumber.txt")
            putExtra("filenameForgetFootballPlayersNames", "players_chapter_$chapterNumber.txt")
            putExtra("filenameForGameProgress", "game_progress.txt")
        }
        getResult.launch(intent)
    }

    private fun showResetDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Reset progress")
            .setMessage("Are you sure you want to discard all game progress? " +
                    "All achievements will be lost.")
            .setPositiveButton("Reset") { _, _ ->
                viewModel.resetProgress {
                    Toast.makeText(this, "Progress reset successfully",
                        Toast.LENGTH_SHORT).show()
                    viewModel.hideOverlay()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProgress()
    }
}
