package com.dhethi.jntuhconnect.presentation.profile

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.R
import com.dhethi.jntuhconnect.common.ContentData
import com.dhethi.jntuhconnect.presentation.Screen
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.SectionHeader
import com.dhethi.jntuhconnect.presentation.components.SegmentedTabs
import com.dhethi.jntuhconnect.presentation.components.StatusBarScrim
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.home.RecentStudentCard
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode
import com.dhethi.jntuhconnect.presentation.theme.brandGradient
import androidx.compose.foundation.isSystemInDarkTheme

private val themeOptions = listOf("System", "Light", "Dark")

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onOpenStudent: (String) -> Unit,
    onOpenRoute: (String) -> Unit
) {
    val context = LocalContext.current
    val themeMode by viewModel.themeMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val students = viewModel.students.value

    val version = remember(context) {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: ""
    }

    val listState = rememberLazyListState()
    val headerScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex >= 1 } }

    Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Dimens.spaceXxl)
    ) {
        item { ProfileHeader(version) }

        // Appearance
        item {
            SectionHeader(
                "Appearance",
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceMd)
            )
        }
        item {
            AppCard(modifier = Modifier.padding(horizontal = Dimens.space)) {
                Text(
                    "Theme",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(Dimens.spaceSm))
                SegmentedTabs(
                    options = themeOptions,
                    selected = themeMode.label(),
                    onSelect = { viewModel.setThemeMode(it.toThemeMode()) }
                )
            }
        }

        // Notifications
        item {
            SectionHeader(
                "Notifications",
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceMd)
            )
        }
        item {
            AppCard(modifier = Modifier.padding(horizontal = Dimens.space)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(Dimens.space))
                    Column(Modifier.weight(1f)) {
                        Text("Result alerts", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Get notified when new results are published",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { enabled ->
                            viewModel.setNotificationsEnabled(enabled)
                            if (!enabled) openAppNotificationSettings(context)
                        }
                    )
                }
            }
        }

        // Saved students
        if (students.isNotEmpty()) {
            item {
                SectionHeader(
                    "Saved students",
                    modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceMd)
                )
            }
            items(students, key = { it.rollNumber }) { student ->
                RecentStudentCard(
                    student = student,
                    onClick = { onOpenStudent(student.rollNumber) },
                    modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceXs)
                )
            }
        }

        // Resources
        item {
            SectionHeader(
                "More",
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceMd)
            )
        }
        item {
            AppCard(
                modifier = Modifier.padding(horizontal = Dimens.space),
                contentPadding = PaddingValues(vertical = Dimens.spaceXs)
            ) {
                LinkRow(Icons.Rounded.Campaign, "Channels", "Telegram, WhatsApp & more") {
                    onOpenRoute(Screen.Channels.route)
                }
                LinkRow(Icons.AutoMirrored.Rounded.HelpOutline, "Help & FAQ", "Answers to common questions") {
                    onOpenRoute(Screen.Help.route)
                }
                LinkRow(Icons.Rounded.Share, "Share the app", "Tell your classmates") {
                    shareApp(context)
                }
            }
        }

        // Connect
        item {
            SectionHeader(
                "Connect",
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceMd)
            )
        }
        item {
            AppCard(
                modifier = Modifier.padding(horizontal = Dimens.space),
                contentPadding = PaddingValues(vertical = Dimens.spaceXs)
            ) {
                ContentData.socials.forEach { social ->
                    LinkRow(Icons.Rounded.OpenInNew, social.name, social.url) {
                        openCustomTab(context, social.url)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(Dimens.spaceLg))
            Text(
                "Made with ❤️ for JNTUH students" + if (version.isNotBlank()) "  ·  v$version" else "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.space),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
        StatusBarScrim(visible = headerScrolled)
    }
}

@Composable
private fun ProfileHeader(version: String) {
    val dark = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brandGradient(dark))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Dimens.spaceLg)
                .padding(top = Dimens.spaceLg, bottom = Dimens.spaceXl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.width(Dimens.space))
            Column {
                Text(
                    "JNTUH Connect",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (version.isNotBlank()) "Version $version" else "Your JNTUH companion",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun LinkRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.spaceMd, vertical = Dimens.spaceMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(Dimens.space))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

private fun ThemeMode.label(): String = when (this) {
    ThemeMode.SYSTEM -> "System"
    ThemeMode.LIGHT -> "Light"
    ThemeMode.DARK -> "Dark"
}

private fun String.toThemeMode(): ThemeMode = when (this) {
    "Light" -> ThemeMode.LIGHT
    "Dark" -> ThemeMode.DARK
    else -> ThemeMode.SYSTEM
}

private fun shareApp(context: android.content.Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Check your JNTUH results instantly with JNTUH Connect — ${ContentData.WEB_BASE}"
        )
    }
    context.startActivity(Intent.createChooser(intent, "Share JNTUH Connect"))
}

private fun openAppNotificationSettings(context: android.content.Context) {
    runCatching {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }
}
