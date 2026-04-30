package com.example.guessplayer.domain.model

class FootballClub(val clubImage: Int?, val transferYear: String) {
    fun isLoanTransfer(): Boolean {
        return transferYear.contains("_(L)")
    }

    fun getCleanTransferYear(): String {
        return transferYear.replace("_(L)", "")
    }
}
