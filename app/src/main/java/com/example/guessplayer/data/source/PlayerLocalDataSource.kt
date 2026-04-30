package com.example.guessplayer.data.source

import android.content.Context

class PlayerLocalDataSource(private val context: Context) {

    fun getNames(filename: String): List<String> {
        return context.assets.open(filename).bufferedReader().useLines { it ->
            it.map { line -> line.trim() }
                .filter { it.isNotEmpty() }
                .toList()
        }
    }

    fun getClubs(filename: String): List<List<String>> {
        return context.assets.open(filename).bufferedReader().readLines()
            .map { it.trim().split("\\s+".toRegex()) }
    }

    fun getYears(filename: String): List<List<String>> {
        return context.assets.open(filename).bufferedReader().readLines()
            .map { it.trim().split("\\s+".toRegex()) }
    }
}
