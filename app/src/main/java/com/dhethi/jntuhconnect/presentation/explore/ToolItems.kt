package com.dhethi.jntuhconnect.presentation.explore

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.LibraryBooks
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.common.ContentData
import com.dhethi.jntuhconnect.presentation.Screen

/** What happens when a tool is tapped. */
sealed interface ToolAction {
    /** Ask for a roll number, then open the student result screen on [tab]. */
    data class StudentTab(val tab: String) : ToolAction
    /** Navigate to an in-app route. */
    data class Route(val route: String) : ToolAction
    /** Open an external URL. */
    data class External(val url: String) : ToolAction
}

data class ToolItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: Color,
    val action: ToolAction
)

// A varied accent palette so the grid reads as colorful but coherent.
private val cEmerald = Color(0xFF0E9F6E)
private val cTeal = Color(0xFF0E7C86)
private val cBlue = Color(0xFF2563EB)
private val cAmber = Color(0xFFF59E0B)
private val cViolet = Color(0xFF7C3AED)
private val cRose = Color(0xFFE11D48)
private val cCyan = Color(0xFF0891B2)
private val cGreen = Color(0xFF16A34A)
private val cIndigo = Color(0xFF4F46E5)

/** Tools that operate on a single student's result. */
val resultTools = listOf(
    ToolItem(
        "Academic Result", "Consolidated CGPA & mark sheet",
        Icons.Rounded.School, cEmerald, ToolAction.StudentTab(Constants.ACADEMIC_RESULTS)
    ),
    ToolItem(
        "All Results", "Every attempt, semester by semester",
        Icons.Rounded.LibraryBooks, cTeal, ToolAction.StudentTab(Constants.ALL_RESULTS)
    ),
    ToolItem(
        "Backlog Report", "Subjects still to be cleared",
        Icons.Rounded.ErrorOutline, cRose, ToolAction.StudentTab(Constants.BACKLOG_RESULTS)
    ),
    ToolItem(
        "Credits Checker", "Obtained vs required credits",
        Icons.Rounded.CreditCard, cAmber, ToolAction.StudentTab(Constants.CREDIT_RESULTS)
    )
)

/** Tools with their own dedicated flow. */
val analysisTools = listOf(
    ToolItem(
        "Result Contrast", "Compare two students side by side",
        Icons.Rounded.CompareArrows, cViolet, ToolAction.Route(Screen.ResultContrast.route)
    ),
    ToolItem(
        "Class Result", "Rank an entire class section",
        Icons.Rounded.Groups, cBlue, ToolAction.Route(Screen.ClassResult.route)
    ),
    ToolItem(
        "Grace Marks", "Check eligibility & upload proof",
        Icons.Rounded.WorkspacePremium, cGreen, ToolAction.Route(Screen.GraceMarks.route)
    )
)

/** Info & resource tools. */
val resourceTools = listOf(
    ToolItem(
        "Updates", "Latest result notifications",
        Icons.Rounded.Notifications, cCyan, ToolAction.Route(Screen.Updates.route)
    ),
    ToolItem(
        "Calendars", "Academic calendars",
        Icons.Rounded.CalendarMonth, cIndigo, ToolAction.Route(Screen.Calendars.route)
    ),
    ToolItem(
        "Syllabus", "Regulation-wise syllabus",
        Icons.Rounded.MenuBook, cTeal, ToolAction.Route(Screen.Syllabus.route)
    ),
    ToolItem(
        "Jobs & Careers", "Opportunities for students",
        Icons.Rounded.WorkOutline, cAmber, ToolAction.Route(Screen.Careers.route)
    ),
    ToolItem(
        "Important Questions", "Exam prep question bank",
        Icons.Rounded.Quiz, cViolet, ToolAction.External(ContentData.IMP_QUESTIONS_URL)
    ),
    ToolItem(
        "Channels", "Telegram, WhatsApp & more",
        Icons.Rounded.Campaign, cRose, ToolAction.Route(Screen.Channels.route)
    ),
    ToolItem(
        "Help Center", "FAQ & contact",
        Icons.Rounded.HelpOutline, cBlue, ToolAction.Route(Screen.Help.route)
    )
)

/** Quick tools surfaced on the Home screen. */
val homeQuickTools = listOf(
    analysisTools[0], // Result Contrast
    analysisTools[1], // Class Result
    analysisTools[2], // Grace Marks
    resultTools[3]    // Credits Checker
)
