package com.pph.nqueens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pph.nqueens.testing.*
import com.pph.shared.testing.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class WinAndPersistenceUiTest {

    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1) val rule = createAndroidComposeRule<com.pph.nqueens.ui.MainActivity>()

    @Before fun setup() {
        hiltRule.inject()
        TestStorage.clearGamePrefsDataStore(rule.activity)

        rule.activityRule.scenario.recreate()
        rule.waitForIdle()
    }

    private fun startN4(name: String) {
        rule.enterPlayerName(name)
        rule.selectN(4)
        rule.onNodeWithTag(TestTags.SETBOARD_PLAY_BTN).performClick()
        rule.waitForIdle()
        rule.onNodeWithTag(TestTags.GAME_BOARD).assertIsDisplayed()
    }

    @Test fun solvingN4_showsWinDialog() {
        startN4("Winner")

        rule.solveN4_solutionA()
        rule.waitForIdle()

        rule.onNodeWithTag(TestTags.WIN_DIALOG).assertIsDisplayed()
        rule.onNodeWithTag(TestTags.WIN_TITLE).assertTextContains("Well done", substring = true)
        rule.onNodeWithTag(TestTags.WIN_TIME).assertIsDisplayed()
    }

    @Test fun playAgain_fromWinDialog_resetsBoard_andHidesDialog() {
        startN4("Winner")

        rule.solveN4_solutionA()
        rule.waitForIdle()
        rule.onNodeWithTag(TestTags.WIN_DIALOG).assertIsDisplayed()

        rule.onNodeWithTag(TestTags.WIN_PLAY_AGAIN).performClick()
        rule.waitForIdle()

        rule.onNodeWithTag(TestTags.WIN_DIALOG).assertDoesNotExist()
        rule.onAllNodesWithContentDescription("Queen").assertCountEquals(0)
        rule.onNodeWithTag(TestTags.GAME_HEADER_QUEENS).assertTextContains("0 / 4", substring = true)
    }

    @Test fun goBack_fromWinDialog_returnsToSetBoard() {
        startN4("Winner")

        rule.solveN4_solutionA()
        rule.waitForIdle()

        rule.onNodeWithTag(TestTags.WIN_GO_BACK).performClick()
        rule.waitForIdle()

        rule.onNodeWithTag(TestTags.SETBOARD_TITLE).assertIsDisplayed()
    }

    @Test fun finishingGame_savesScore_andShowsInSetBoardScorePanel() {
        startN4("Morrison")

        rule.solveN4_solutionA()
        rule.waitForIdle()

        rule.onNodeWithTag(TestTags.WIN_GO_BACK).performClick()

        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag(TestTags.SCORES_LIST, useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithTag(TestTags.SCORES_LIST).performScrollToNode(
            hasTestTag(TestTags.scoreItem(1))
        )

        rule.onNodeWithTag(TestTags.scoreItem(1)).assertIsDisplayed()
        rule.onNodeWithTag(TestTags.scoreItem(1)).assertTextContains("Morrison -", substring = true)
    }
}
