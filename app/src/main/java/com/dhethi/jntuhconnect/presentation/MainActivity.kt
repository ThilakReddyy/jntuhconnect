package com.dhethi.jntuhconnect.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dhethi.jntuhconnect.presentation.components.CustomButtonBar
import com.dhethi.jntuhconnect.presentation.components.CustomTopBar
import com.dhethi.jntuhconnect.presentation.jobs.JobsScreen
import com.dhethi.jntuhconnect.presentation.pdf.PdfScreen
import com.dhethi.jntuhconnect.presentation.profile.ProfileScreen
import com.dhethi.jntuhconnect.presentation.results.ResultScreen
import com.dhethi.jntuhconnect.presentation.studentResult.StudentResultScreen
import com.dhethi.jntuhconnect.presentation.theme.JntuhConnectTheme
import com.dhethi.jntuhconnect.presentation.updates.UpdatesScreen
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
//
//            val token = task.result
//
//            Log.d("FCM_TOKEN", "FCM Token: $token")
        }
        setContent {
            RequestNotificationPermission()

            JntuhConnectTheme {


                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                val fullScreenRoutes = listOf("studentResults/{rollNumber}")

                fun navigate(route: String) {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                fun navigateBack() {
                    navController.popBackStack()
                }

                val isFullScreen = currentRoute?.let { route ->
                    fullScreenRoutes.any { fullRoute ->
                        route.startsWith(fullRoute.removeSuffix("/{rollNumber}"))
                    }
                } ?: false

                val navGraph: @Composable (Modifier) -> Unit = { modifier ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ResultsScreen.route,
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        composable(Screen.ResultsScreen.route) {
                            ResultScreen(navigate = { navigate(it) })
                        }
                        composable(Screen.UpdatesScreen.route) {
                            UpdatesScreen(navigateBack = { navigateBack() })
                        }
                        composable(Screen.JobsScreen.route) {
                            JobsScreen()
                        }
                        composable(Screen.ProfileScreen.route) {
                            ProfileScreen()
                        }

                        composable(
                            route = Screen.StudentResultsScreen.route + "/{rollNumber}",
                            arguments = listOf(
                                navArgument("rollNumber") { type = NavType.Companion.StringType }
                            )
                        ) { backStackEntry ->
                            StudentResultScreen(navigateBack = { navigateBack() })
                        }
                    }
                }

                if (isFullScreen) {
                    navGraph(Modifier.Companion)
                } else {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.background,
                        topBar = { CustomTopBar(navController) },
                        bottomBar = { CustomButtonBar(navController, currentRoute) }
                    ) { innerPadding ->
                        navGraph(Modifier.Companion.padding(innerPadding))
                    }
                }
            }
        }
    }
}


@Composable
fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                Log.d("FCM_PERMISSION", "Granted: $granted")
            }
        )
        LaunchedEffect(Unit) { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }
    }
}