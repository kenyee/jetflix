package com.yasinkacmaz.jetflix.main

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.yasinkacmaz.jetflix.BuildConfig
import com.yasinkacmaz.jetflix.testing.androidtest.compose.waitUntilDoesNotExist
import com.yasinkacmaz.jetflix.testing.androidtest.compose.waitUntilExists
import com.yasinkacmaz.jetflix.testing.androidtest.mockweb.MockWebServerInstrumentationRule
import com.yasinkacmaz.jetflix.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val mockWebServerRule = MockWebServerInstrumentationRule(MainActivityTest::class, BuildConfig.MOCKSERVER_PORT)

    @Before
    fun setUp() {
        mockWebServerRule.responseFiles = mapOf(
            "/genre/movie/list" to "genre_movie_list_success.json",
            "/discover/movie?page=1&" to "discover_movie_success_1.json"
        )
    }

    @Test
    fun onStart_shouldShowSearchBarAndFetchingMsg() {
        composeTestRule.waitForIdle()
        composeTestRule.waitUntilExists(hasText("Fetching Movies"), 5000)
        composeTestRule.onNodeWithText("Search Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fetching Movies").assertIsDisplayed()
    }

    @Test
    fun onStart_shouldShowSearchBarAndMovieList() {
        composeTestRule.waitForIdle()
        composeTestRule.waitUntilExists(hasText("Fetching Movies"), 5000)
        composeTestRule.onNodeWithText("Search Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fetching Movies").assertIsDisplayed()
        composeTestRule.waitUntilDoesNotExist(hasText("Fetching Movies"), 5000)
        composeTestRule.onNodeWithText("Search Movies").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MoviesGrid").assertIsDisplayed()
        // this checks that there is at least one movie in the grid;
        // we can't check a specific movie or this test will become flaky if server response changes
        composeTestRule.onAllNodesWithTag("MovieContent")
            .assertAny(hasTestTag("MovieContent"))
        // this check is stable only if we use mock responses
        composeTestRule.onNodeWithText("Black Adam").assertIsDisplayed()
    }

    @Test
    fun onStart_showErrorWhenReceivingError() {
        // NOTE: this test will fail if the mock web server is not used
        mockWebServerRule.errorCode = 404
        composeTestRule.waitForIdle()
        composeTestRule.waitUntilExists(hasText("Fetching Movies"), 5000)
        composeTestRule.onNodeWithText("Search Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fetching Movies").assertIsDisplayed()
        composeTestRule.waitUntilExists(hasText("HTTP 404 Client Error"), 5000)
    }
}
