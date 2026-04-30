package com.example.guessplayer.data.source

import android.content.Context
import java.io.FileNotFoundException

class ProgressLocalDataSource(private val context: Context) {

    private val fileName = "game_progress.txt"

    fun readAll(): List<String> {
        return try {
            context.openFileInput(fileName).bufferedReader().readLines()
        } catch (e: FileNotFoundException) {
            createDefault()
        }
    }

    private fun createDefault(): List<String> {
        val default = listOf(
            "chapter1 1",
            "chapter2 1",
            "chapter3 1",
            "balance 0",
            "user_level 0",
            "selection 0"
        )

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(default.joinToString("\n").toByteArray())
        }

        return default
    }

    fun write(lines: List<String>) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(lines.joinToString("\n").toByteArray())
        }
    }
}
