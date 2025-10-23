package com.dhethi.jntuhconnect.presentation

sealed class Screen(val route:String) {
    object ResultsScreen: Screen("results")
    object UpdatesScreen: Screen("updates")
    object JobsScreen: Screen("jobs")
    object ProfileScreen: Screen("profile")
    object StudentResultsScreen: Screen("studentResults")
}