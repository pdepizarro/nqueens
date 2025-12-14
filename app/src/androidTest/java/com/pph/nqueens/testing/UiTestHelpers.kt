package com.pph.nqueens.testing

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.pph.nqueens.ui.MainActivity
import com.pph.shared.testing.TestTags

typealias MainRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun MainRule.enterPlayerName(name: String) {
    onNodeWithTag(TestTags.SETBOARD_PLAYER_FIELD).performTextClearance()
    onNodeWithTag(TestTags.SETBOARD_PLAYER_FIELD).performTextInput(name)
}

fun MainRule.selectN(n: Int) {
    onNodeWithTag(TestTags.SETBOARD_N_DROPDOWN, useUnmergedTree = true)
        .assertExists()
        .performClick()

    onNodeWithTag("${TestTags.SETBOARD_N_DROPDOWN}:option:$n", useUnmergedTree = true)
        .assertExists()
        .performClick()

    waitForIdle()
}

fun MainRule.tapCell(row: Int, col: Int) {
    onNodeWithTag(TestTags.boardCell(row, col)).performClick()
}

fun MainRule.solveN4_solutionA() {
    tapCell(0, 1)
    tapCell(1, 3)
    tapCell(2, 0)
    tapCell(3, 2)
}