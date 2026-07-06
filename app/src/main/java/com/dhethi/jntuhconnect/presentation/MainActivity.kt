package com.dhethi.jntuhconnect.presentation

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dhethi.jntuhconnect.presentation.components.CustomBottomBar
import com.dhethi.jntuhconnect.presentation.content.CalendarsScreen
import com.dhethi.jntuhconnect.presentation.content.CareersScreen
import com.dhethi.jntuhconnect.presentation.content.ChannelsScreen
import com.dhethi.jntuhconnect.presentation.content.HelpScreen
import com.dhethi.jntuhconnect.presentation.content.SyllabusScreen
import com.dhethi.jntuhconnect.presentation.chatbot.ChatbotScreen
import com.dhethi.jntuhconnect.presentation.classresult.ClassResultScreen
import com.dhethi.jntuhconnect.presentation.contrast.ResultContrastScreen
import com.dhethi.jntuhconnect.presentation.explore.ExploreScreen
import com.dhethi.jntuhconnect.presentation.gracemarks.GraceMarksScreen
import com.dhethi.jntuhconnect.presentation.home.HomeScreen
import com.dhethi.jntuhconnect.presentation.profile.ProfileScreen
import com.dhethi.jntuhconnect.presentation.studentResult.StudentResultScreen
import com.dhethi.jntuhconnect.presentation.theme.JntuhConnectTheme
import com.dhethi.jntuhconnect.presentation.update.InAppUpdateHandler
import com.dhethi.jntuhconnect.presentation.updates.UpdatesScreen
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchFcmToken()
        setContent {
            val appViewModel: AppViewModel = hiltViewModel()
            val themeMode by appViewModel.themeMode.collectAsState()
            RequestNotificationPermission()
            JntuhConnectTheme(themeMode = themeMode) {
                AppNavigation()
            }
        }
    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Fetching FCM token failed", task.exception)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isTopLevel = currentRoute in Screen.topLevelRoutes
    val snackbarHostState = remember { SnackbarHostState() }

    // Nudges the user to update once a newer version is live on the Play Store.
    InAppUpdateHandler(snackbarHostState)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (isTopLevel) {
                CustomBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { navController.navigateSingleTop(it) }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = { fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 12 } },
        exitTransition = { fadeOut(tween(160)) },
        popEnterTransition = { fadeIn(tween(220)) },
        popExitTransition = { fadeOut(tween(160)) + slideOutHorizontally(tween(200)) { it / 12 } }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenStudent = { roll -> navController.navigate("${Screen.StudentResults.route}/$roll") },
                onOpenStudentTab = { roll, tab ->
                    navController.navigate("${Screen.StudentResults.route}/$roll?startTab=${Uri.encode(tab)}")
                },
                onOpenRoute = { route -> navController.navigate(route) }
            )
        }
        composable(Screen.Explore.route) {
            ExploreScreen(
                onOpenStudentTab = { roll, tab ->
                    navController.navigate("${Screen.StudentResults.route}/$roll?startTab=${Uri.encode(tab)}")
                },
                onOpenRoute = { route -> navController.navigate(route) }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onOpenStudent = { roll -> navController.navigate("${Screen.StudentResults.route}/$roll") },
                onOpenRoute = { route -> navController.navigate(route) }
            )
        }
        composable(Screen.Updates.route) {
            UpdatesScreen(navigateBack = { navController.popBackStack() })
        }
        composable(
            route = "${Screen.StudentResults.route}/{rollNumber}?startTab={startTab}",
            arguments = listOf(
                navArgument("rollNumber") { type = NavType.StringType },
                navArgument("startTab") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            StudentResultScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.ResultContrast.route) {
            ResultContrastScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.ClassResult.route) {
            ClassResultScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.GraceMarks.route) {
            GraceMarksScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.Calendars.route) {
            CalendarsScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.Syllabus.route) {
            SyllabusScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.Channels.route) {
            ChannelsScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.Careers.route) {
            CareersScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.Help.route) {
            HelpScreen(navigateBack = { navController.popBackStack() })
        }
        composable(Screen.Chatbot.route) {
            ChatbotScreen(navigateBack = { navController.popBackStack() })
        }
    }
}

fun NavController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted -> Log.d("FCM_PERMISSION", "Granted: $granted") }
        )
        LaunchedEffect(Unit) { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }
    }
}
