package com.example.guessplayer.data.repository

import com.example.guessplayer.data.source.ProgressLocalDataSource
import com.example.guessplayer.domain.model.GameProgress
import com.example.guessplayer.domain.repository.ProgressRepository

class ProgressRepositoryImpl(
    private val dataSource: ProgressLocalDataSource
) : ProgressRepository {

    override fun getProgress(): GameProgress {
        val lines = dataSource.readAll()

        val chapters = mutableMapOf<Int, Int>()
        var balance = 0
        var userLevel = 0
        var selection = 0

        for (line in lines) {
            val parts = line.split(" ")
            if (parts.size != 2) continue

            when {
                parts[0].startsWith("chapter") -> {
                    val num = parts[0].removePrefix("chapter").toInt()
                    chapters[num] = parts[1].toInt()
                }
                parts[0] == "balance" -> balance = parts[1].toInt()
                parts[0] == "user_level" -> userLevel = parts[1].toInt()
                parts[0] == "selection" -> selection = parts[1].toInt()
            }
        }

        return GameProgress(chapters, balance, userLevel, selection)
    }

    override fun updateProgress(progress: GameProgress) {
        val lines = mutableListOf<String>()

        progress.chapters.forEach { (k, v) ->
            lines.add("chapter$k $v")
        }

        lines.add("balance ${progress.balance}")
        lines.add("user_level ${progress.userLevel}")
        lines.add("selection ${progress.selection}")

        dataSource.write(lines)
    }

    override fun resetProgress() {
        val defaultProgress = GameProgress(
            chapters = mapOf(
                1 to 0,
                2 to 0,
                3 to 0,
                4 to 0,
                5 to 0
            ),
            balance = 0,
            userLevel = 0,
            selection = 0
        )

        updateProgress(defaultProgress)
    }
}
