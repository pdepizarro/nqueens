package com.pph.shared.testing

object TestTags {
    // --- SetBoard ---
    const val SETBOARD_TITLE = "setboard:title"
    const val SETBOARD_PLAYER_FIELD = "setboard:playerField"
    const val SETBOARD_PLAYER_ERROR = "setboard:playerError"
    const val SETBOARD_N_DROPDOWN = "setboard:nDropdown"
    const val SETBOARD_RANDOM_BTN = "setboard:randomBtn"
    const val SETBOARD_PLAY_BTN = "setboard:playBtn"

    const val SCORES_PANEL = "scores:panel"
    const val SCORES_TITLE = "scores:title"
    const val SCORES_EMPTY = "scores:empty"
    const val SCORES_LIST = "scores:list"
    fun scoreItem(position: Int) = "scores:item:$position"

    // --- Game ---
    const val GAME_HEADER_TIME = "game:headerTime"
    const val GAME_HEADER_QUEENS = "game:headerQueens"
    const val GAME_RESET_BTN = "game:resetBtn"

    const val GAME_BOARD = "game:board"

    const val PROGRESS_INDICATOR = "game:progressIndicator"
    fun boardCell(row: Int, col: Int) = "game:cell:r$row:c$col"

    const val WIN_DIALOG = "win:dialog"
    const val WIN_TITLE = "win:title"
    const val WIN_TIME = "win:time"
    const val WIN_RECORD = "win:record"
    const val WIN_PLAY_AGAIN = "win:playAgain"
    const val WIN_GO_BACK = "win:goBack"
}
