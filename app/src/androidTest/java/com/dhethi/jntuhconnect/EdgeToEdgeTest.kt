package com.dhethi.jntuhconnect

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dhethi.jntuhconnect.presentation.MainActivity
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EdgeToEdgeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun profileHeaderAndBottomNavigationAvoidSystemBars() {
        composeRule.onNodeWithText("Profile").performClick()
        composeRule.waitForIdle()

        val decorView = composeRule.activity.window.decorView
        val rootInsets = checkNotNull(ViewCompat.getRootWindowInsets(decorView))
        val topInset = rootInsets.getInsets(
            WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.displayCutout()
        ).top
        val bottomInset = rootInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

        val headerBounds = composeRule
            .onNodeWithText("Profile & settings")
            .fetchSemanticsNode()
            .boundsInRoot
        val profileTabBounds = composeRule
            .onNodeWithText("Profile")
            .fetchSemanticsNode()
            .boundsInRoot

        assertTrue(
            "Profile header overlaps the status bar or display cutout",
            headerBounds.top >= topInset
        )
        assertTrue(
            "Profile navigation action overlaps the navigation bar",
            profileTabBounds.bottom <= decorView.height - bottomInset
        )
    }
}
