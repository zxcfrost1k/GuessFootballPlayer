package com.example.guessplayer.presentation.chapter

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.guessplayer.R
import com.example.guessplayer.domain.model.MainViewModel

class FragmentForChapter4 : Fragment(R.layout.chapter_fragment_4) {

    private lateinit var mainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val btn = view.findViewById<Button>(R.id.butOfChap4)
        btn.setOnClickListener {
            mainViewModel.onChapterSelected(4)
        }
    }
}