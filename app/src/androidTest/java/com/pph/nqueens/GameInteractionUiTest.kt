package com.pph.nqueens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pph.nqueens.testing.TestStorage
import com.pph.nqueens.testing.enterPlayerName
import com.pph.nqueens.testing.selectN
import com.pph.nqueens.testing.tapCell
import com.pph.shared.testing.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GameInteractionUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<com.pph.nqueens.ui.MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        TestStorage.clearGamePrefsDataStore(rule.activity)
        rule.activityRule.scenario.recreate()
        rule.waitForIdle()
    }


    private fun goToGame(boardN: Int, playerName: String) {
        rule.enterPlayerName(playerName)
        rule.selectN(boardN)
        rule.onNodeWithTag(TestTags.SETBOARD_PLAY_BTN).performClick()

        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag(TestTags.GAME_BOARD).fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithTag(TestTags.GAME_BOARD).assertIsDisplayed()
    }


    @Test
    fun tapCell_placesQueen_andTapAgain_removesIt() {
        goToGame(4, "A")

        rule.tapCell(0, 0)
        rule.waitForIdle()
        rule.onAllNodesWithContentDescription("Queen").assertCountEquals(1)

        rule.tapCell(0, 0)
        rule.waitForIdle()
        rule.onAllNodesWithContentDescription("Queen").assertCountEquals(0)
    }

    @Test
    fun cannotPlaceMoreThanNQueens() {
        goToGame(4, "A")

        rule.tapCell(0, 0)
        rule.tapCell(1, 1)
        rule.tapCell(2, 2)
        rule.tapCell(3, 3)
        rule.waitForIdle()
        rule.onAllNodesWithContentDescription("Queen").assertCountEquals(4)

        rule.tapCell(0, 1)
        rule.waitForIdle()
        rule.onAllNodesWithContentDescription("Queen").assertCountEquals(4)
    }

    @Test
    fun resetButton_clearsQueens() {
        goToGame(4, "A")

        rule.tapCell(0, 0)
        rule.tapCell(1, 2)
        rule.waitForIdle()
        rule.onAllNodesWithContentDescription("Queen").assertCountEquals(2)

        rule.onNodeWithTag(TestTags.GAME_RESET_BTN).performClick()
        rule.waitForIdle()
        rule.onAllNodesWithContentDescription("Queen").assertCountEquals(0)
    }
}
