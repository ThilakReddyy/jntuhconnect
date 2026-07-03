package com.dhethi.jntuhconnect.presentation

sealed class Screen(val route: String) {
    // Top-level tabs
    data object Home : Screen("home")
    data object Explore : Screen("explore")
    data object Profile : Screen("profile")

    // Pushed destinations
    data object Updates : Screen("updates")
    data object StudentResults : Screen("studentResults")
    data object ResultContrast : Screen("resultContrast")
    data object ClassResult : Screen("classResult")
    data object GraceMarks : Screen("graceMarks")
    data object Calendars : Screen("calendars")
    data object Syllabus : Screen("syllabus")
    data object Channels : Screen("channels")
    data object Careers : Screen("careers")
    data object Help : Screen("help")

    companion object {
        /** Routes that show the bottom navigation bar. */
        val topLevelRoutes = setOf(Home.route, Explore.route, Profile.route)
    }
}
