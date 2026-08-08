package com.dhethi.jntuhconnect

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import com.dhethi.jntuhconnect.presentation.MainActivity
import com.dhethi.jntuhconnect.service.MyFirebaseMessagingService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Before
    fun grantNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.uiAutomation.grantRuntimePermission(
                instrumentation.targetContext.packageName,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    @Test
    fun topLevelNavigationAndHomeValidationWork() {
        compose.onNodeWithText("Your academic record", substring = true).assertIsDisplayed()
        compose.onNodeWithContentDescription("Search").performClick()
        compose.onNodeWithText("Roll number cannot be empty").assertIsDisplayed()

        openExplore()
        compose.onNodeWithText("Profile").performClick()
        compose.onNodeWithText("Profile & settings").assertIsDisplayed()
        compose.onNodeWithText("Appearance").assertIsDisplayed()
        compose.onNodeWithText("Notifications").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Result alerts").assertIsDisplayed()
        compose.onNodeWithText("Light").performClick()
        compose.onNodeWithText("Dark").performClick()
        compose.onNodeWithText("System").performClick()
        compose.onNodeWithText("Home").performClick()
        compose.onNodeWithText("Your academic record", substring = true).assertIsDisplayed()
    }

    @Test
    fun studentAndAnalysisFlowsAreReachableAndBackNavigationWorks() {
        openExplore()

        compose.onNodeWithText("Academic Result").performScrollTo().performClick()
        compose.onNodeWithText("Enter roll number").assertIsDisplayed()
        compose.onNode(hasSetTextAction()).performTextInput("18E51A0479")
        compose.onNodeWithText("Continue").performClick()
        compose.onNodeWithText("Student Result").assertIsDisplayed()
        compose.waitUntil(timeoutMillis = 30_000) {
            compose.onAllNodesWithText("All Results").fetchSemanticsNodes().isNotEmpty()
        }
        compose.onNodeWithText("All Results").performClick()
        compose.waitForIdle()
        compose.onNodeWithContentDescription("Back").performClick()
        compose.onNodeWithText("Profile").assertIsDisplayed()

        scrollExploreToAndClick("Result Contrast")
        compose.onNodeWithText("First roll number").performTextInput("18E51A0479")
        compose.onNodeWithText("Second roll number").performTextInput("18E51A0473")
        compose.onNodeWithText("Compare").assertIsEnabled()
        compose.onNodeWithContentDescription("Back").performClick()

        scrollExploreToAndClick("Class Result")
        compose.onNodeWithText("Roll Number").performTextInput("18E51A0479")
        compose.onNodeWithText("Load class").assertIsEnabled()
        compose.onNodeWithContentDescription("Back").performClick()

        scrollExploreToAndClick("Grace Marks")
        compose.onNodeWithText("Roll Number").performTextInput("18E51A0473")
        compose.onNodeWithText("Check eligibility").assertIsEnabled()
        compose.onNodeWithContentDescription("Back").performClick()
    }

    @Test
    fun updatesAndFilterControlsAreReachable() {
        openExplore()
        scrollExploreToAndClick("Updates")
        compose.onNodeWithContentDescription("Filter").performClick()
        compose.onNodeWithText("Filter updates").assertIsDisplayed()
        compose.onNodeWithText("Apply").performScrollTo().assertIsDisplayed()
        pressBack()
        compose.onNodeWithText("Updates").assertIsDisplayed()
        pressBack()
        compose.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun notificationTapOpensUpdates() {
        lateinit var launchIntent: Intent
        compose.activityRule.scenario.onActivity { activity ->
            launchIntent = activity.intent
            InstrumentationRegistry.getInstrumentation().callActivityOnNewIntent(
                activity,
                Intent(activity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    putExtra(
                        MyFirebaseMessagingService.EXTRA_NOTIFICATION_DESTINATION,
                        MyFirebaseMessagingService.DESTINATION_UPDATES
                    )
                }
            )
        }

        compose.onNodeWithText("Updates").assertIsDisplayed()
        compose.onNodeWithContentDescription("Filter").assertIsDisplayed()
        compose.activityRule.scenario.onActivity { activity ->
            activity.intent = launchIntent
        }
    }

    @Test
    fun resourceAndAssistantScreensAreReachable() {
        openExplore()
        openRouteAndReturn("AI Assistant")
        openRouteAndReturn("Calendars")
        openRouteAndReturn("Syllabus")
        openRouteAndReturn("Jobs & Careers")
        openRouteAndReturn("Channels")
        openRouteAndReturn("Help Center")
    }

    private fun openExplore() {
        compose.onNodeWithText("Explore").performClick()
        compose.onNodeWithText("Every JNTUH tool in one place").assertIsDisplayed()
    }

    private fun openRouteAndReturn(title: String) {
        scrollExploreToAndClick(title)
        compose.onNodeWithContentDescription("Back").assertIsDisplayed()
        compose.onNodeWithContentDescription("Back").performClick()
        compose.onNodeWithText("Profile").assertIsDisplayed()
    }

    private fun scrollExploreToAndClick(title: String) {
        compose.onNode(hasScrollAction()).performScrollToNode(hasText(title))
        compose.onNodeWithText(title).performClick()
    }
}
