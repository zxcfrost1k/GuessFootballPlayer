package com.example.guessplayer.RECAST.chapter_tools_RECAST

class FootballClub(val clubImage: Int?, val transferYear: String) {
    fun isLoanTransfer(): Boolean {
        return transferYear.contains("_(L)")
    }

    fun getCleanTransferYear(): String {
        return transferYear.replace("_(L)", "")
    }
}
