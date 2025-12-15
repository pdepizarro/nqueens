package com.pph.nqueens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pph.nqueens.testing.TestStorage
import com.pph.nqueens.testing.enterPlayerName
import com.pph.shared.testing.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationAndSmokeUiTest {

    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1) val rule = createAndroidComposeRule<com.pph.nqueens.ui.MainActivity>()

    @Before fun setup() {
        hiltRule.inject()
        TestStorage.clearGamePrefsDataStore(rule.activity)
    }

    @Test fun letsGo_navigatesToGameScreen() {
        rule.enterPlayerName("Morrison")
        rule.onNodeWithTag(TestTags.SETBOARD_PLAY_BTN).performClick()

        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag(TestTags.GAME_BOARD).fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithTag(TestTags.GAME_BOARD).assertIsDisplayed()
        rule.onNodeWithTag(TestTags.GAME_HEADER_QUEENS).assertIsDisplayed()
    }

    @Test fun gameResetButton_exists() {
        rule.enterPlayerName("Morrison")
        rule.onNodeWithTag(TestTags.SETBOARD_PLAY_BTN).performClick()
        rule.onNodeWithTag(TestTags.GAME_RESET_BTN).assertIsDisplayed()
    }
}
