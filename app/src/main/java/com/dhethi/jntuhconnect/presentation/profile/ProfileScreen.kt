package com.dhethi.jntuhconnect.presentation.profile

import android.content.Intent
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.ContentData
import com.dhethi.jntuhconnect.presentation.Screen
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.SectionHeader
import com.dhethi.jntuhconnect.presentation.components.StatusBarScrim
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.ShapeMd
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode

private data class AppearanceOption(
    val mode: ThemeMode,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

private val appearanceOptions = listOf(
    AppearanceOption(
        mode = ThemeMode.SYSTEM,
        title = "System",
        subtitle = "Follow your device appearance",
        icon = Icons.Rounded.Devices
    ),
    AppearanceOption(
        mode = ThemeMode.LIGHT,
        title = "Light",
        subtitle = "Use the light academic palette",
        icon = Icons.Rounded.LightMode
    ),
    AppearanceOption(
        mode = ThemeMode.DARK,
        title = "Dark",
        subtitle = "Use the low-light obsidian palette",
        icon = Icons.Rounded.DarkMode
    )
)

private enum class LinkAction {
    INTERNAL,
    EXTERNAL,
    SHARE
}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onOpenRoute: (String) -> Unit
) {
    val context = LocalContext.current
    val themeMode by viewModel.themeMode.collectAsState()

    val version = remember(context) {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: ""
    }
    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = Dimens.spaceXxl)
        ) {
            item { ProfileHeader() }

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
                    "Choose a theme",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Use your device setting or select a mode",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(Dimens.spaceMd))
                AppearanceChoices(
                    selected = themeMode,
                    onSelect = viewModel::setThemeMode
                )
            }
        }

        // Help and community
        item {
            SectionHeader(
                "Help & community",
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceMd)
            )
        }
        item {
            AppCard(
                modifier = Modifier.padding(horizontal = Dimens.space),
                contentPadding = PaddingValues(vertical = Dimens.spaceXs)
            ) {
                LinkRow(Icons.AutoMirrored.Rounded.HelpOutline, "Help & FAQ", "Answers to common questions") {
                    onOpenRoute(Screen.Help.route)
                }
                SettingsDivider()
                LinkRow(Icons.Rounded.Campaign, "Channels", "Result alerts and student updates") {
                    onOpenRoute(Screen.Channels.route)
                }
                ContentData.socials.forEach { social ->
                    SettingsDivider()
                    LinkRow(
                        icon = Icons.Rounded.Campaign,
                        title = social.name,
                        subtitle = socialSubtitle(social.name),
                        action = LinkAction.EXTERNAL
                    ) {
                        openCustomTab(context, social.url)
                    }
                }
            }
        }

        // About
        item {
            SectionHeader(
                "About",
                modifier = Modifier.padding(horizontal = Dimens.space, vertical = Dimens.spaceMd)
            )
        }
            item {
                AppCard(
                    modifier = Modifier.padding(horizontal = Dimens.space),
                    contentPadding = PaddingValues(vertical = Dimens.spaceXs)
                ) {
                    LinkRow(
                        icon = Icons.Rounded.Share,
                        title = "Share JNTUH Connect",
                        subtitle = "Help classmates find results and resources",
                        action = LinkAction.SHARE
                    ) {
                        shareApp(context)
                    }
                    SettingsDivider()
                    VersionRow(version)
                }
            }
        }

        if (isScrolled) {
            StatusBarScrim(brush = SolidColor(MaterialTheme.colorScheme.background))
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = Dimens.spaceLg)
            .padding(top = Dimens.spaceLg, bottom = Dimens.space)
    ) {
        Text(
            "Profile & settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Preferences, saved students and support",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AppearanceChoices(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceSm)
    ) {
        appearanceOptions.forEach { option ->
            AppearanceChoice(
                option = option,
                selected = selected == option.mode,
                onSelect = { onSelect(option.mode) }
            )
        }
    }
}

@Composable
private fun AppearanceChoice(
    option: AppearanceOption,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.touchTarget)
            .clip(ShapeMd)
            .background(containerColor)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onSelect
            )
            .padding(horizontal = Dimens.spaceMd, vertical = Dimens.spaceSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(Dimens.icon)
        )
        Spacer(Modifier.width(Dimens.spaceMd))
        Column(Modifier.weight(1f)) {
            Text(
                option.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
            Text(
                option.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.78f)
            )
        }
        RadioButton(selected = selected, onClick = null)
    }
}

@Composable
private fun LinkRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    action: LinkAction = LinkAction.INTERNAL,
    onClick: () -> Unit
) {
    val clickLabel = when (action) {
        LinkAction.INTERNAL -> "Open $title"
        LinkAction.EXTERNAL -> "Open $title externally"
        LinkAction.SHARE -> "Share JNTUH Connect"
    }
    val actionIcon = when (action) {
        LinkAction.INTERNAL -> Icons.Rounded.ChevronRight
        LinkAction.EXTERNAL -> Icons.AutoMirrored.Rounded.OpenInNew
        LinkAction.SHARE -> Icons.Rounded.Share
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.touchTarget)
            .clickable(onClickLabel = clickLabel, onClick = onClick)
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(Dimens.spaceSm))
        Icon(
            imageVector = actionIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimens.iconSm)
        )
    }
}

@Composable
private fun VersionRow(version: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.touchTarget)
            .padding(horizontal = Dimens.spaceMd, vertical = Dimens.spaceMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(Dimens.space))
        Column(Modifier.weight(1f)) {
            Text("App version", style = MaterialTheme.typography.titleSmall)
            Text(
                if (version.isNotBlank()) "Version $version" else "Version unavailable",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Dimens.spaceMd),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

private fun socialSubtitle(name: String): String = when {
    name.contains("telegram", ignoreCase = true) -> "Result alerts and student discussions"
    name.contains("whatsapp", ignoreCase = true) -> "Announcements and community updates"
    name.contains("youtube", ignoreCase = true) -> "Guides and academic walkthroughs"
    name.contains("instagram", ignoreCase = true) -> "News and community highlights"
    else -> "Updates and student resources"
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
