package com.dhethi.jntuhconnect.presentation

sealed class Screen(
    val route: String,
    val analyticsName: String
) {
    // Top-level tabs
    data object Home : Screen("home", "home")
    data object Explore : Screen("explore", "explore")
    data object Profile : Screen("profile", "profile")

    // Pushed destinations
    data object Updates : Screen("updates", "updates")
    data object StudentResults : Screen("studentResults", "student_results")
    data object ResultContrast : Screen("resultContrast", "result_contrast")
    data object ClassResult : Screen("classResult", "class_result")
    data object GraceMarks : Screen("graceMarks", "grace_marks")
    data object Calendars : Screen("calendars", "calendars")
    data object Syllabus : Screen("syllabus", "syllabus")
    data object Channels : Screen("channels", "channels")
    data object Careers : Screen("careers", "careers")
    data object Help : Screen("help", "help")
    data object Chatbot : Screen("chatbot", "chatbot")

    companion object {
        val all = listOf(
            Home,
            Explore,
            Profile,
            Updates,
            StudentResults,
            ResultContrast,
            ClassResult,
            GraceMarks,
            Calendars,
            Syllabus,
            Channels,
            Careers,
            Help,
            Chatbot
        )

        /** Routes that show the bottom navigation bar. */
        val topLevelRoutes = setOf(Home.route, Explore.route, Profile.route)

        /** Resolves both simple routes and routes containing arguments. */
        fun fromRoute(route: String?): Screen? {
            val baseRoute = route?.substringBefore('/')?.substringBefore('?')
            return all.firstOrNull { it.route == baseRoute }
        }
    }
}
