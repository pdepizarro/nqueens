package com.pph.nqueens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pph.nqueens.testing.TestStorage
import com.pph.nqueens.testing.enterPlayerName
import com.pph.nqueens.testing.selectN
import com.pph.shared.testing.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SetBoardUiTest {

    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1) val rule = createAndroidComposeRule<com.pph.nqueens.ui.MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        rule.activity.let { TestStorage.clearGamePrefsDataStore(it) }
    }

    @Test
    fun startScreen_showsTitleAndScoresPanel() {
        rule.onNodeWithTag(TestTags.SETBOARD_TITLE).assertIsDisplayed()
        rule.onNodeWithTag(TestTags.SCORES_PANEL).assertIsDisplayed()
        rule.onNodeWithTag(TestTags.SCORES_TITLE).assertTextContains("Scores for N=", substring = true)
    }

    @Test fun playDisabled_untilPlayerNameEntered() {
        rule.enterPlayerName("")
        rule.onNodeWithTag(TestTags.SETBOARD_PLAY_BTN).assertIsNotEnabled()
        rule.enterPlayerName("Alex")
        rule.onNodeWithTag(TestTags.SETBOARD_PLAY_BTN).assertIsEnabled()
    }

    @Test fun dropdownChange_updatesScoresTitle() {
        rule.selectN(4)
        rule.onNodeWithTag(TestTags.SCORES_TITLE)
            .assertTextContains("N=4", substring = true)
        rule.selectN(9)
        rule.onNodeWithTag(TestTags.SCORES_TITLE)
            .assertTextContains("N=9", substring = true)
    }

    @Test fun randomN_changesScoresTitleToSomeValidN() {
        rule.onNodeWithTag(TestTags.SETBOARD_RANDOM_BTN).performClick()
        rule.waitForIdle()
        rule.onNodeWithTag(TestTags.SCORES_TITLE).assertExists()
    }

    @Test fun scoresEmpty_showsNoScoresYet_whenValidNAndNoResults() {
        rule.selectN(5)
        rule.waitForIdle()
        rule.onNodeWithTag(TestTags.SCORES_EMPTY).assertIsDisplayed()
        rule.onNodeWithTag(TestTags.SCORES_EMPTY).assertTextContains("NO SCORES YET", ignoreCase = false)
    }
}