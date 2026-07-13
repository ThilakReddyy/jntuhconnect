package com.dhethi.jntuhconnect.presentation.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.presentation.components.RollInputSheet
import com.dhethi.jntuhconnect.presentation.components.SectionHeader
import com.dhethi.jntuhconnect.presentation.components.StatusBarScrim
import com.dhethi.jntuhconnect.presentation.components.ToolCard
import com.dhethi.jntuhconnect.presentation.components.openCustomTab
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun ExploreScreen(
    onOpenStudentTab: (roll: String, tab: String) -> Unit,
    onOpenRoute: (route: String) -> Unit
) {
    val context = LocalContext.current
    var pendingTab by remember { mutableStateOf<String?>(null) }

    pendingTab?.let { tab ->
        RollInputSheet(
            title = "Enter roll number",
            onDismiss = { pendingTab = null },
            onSubmit = { r ->
                pendingTab = null
                onOpenStudentTab(r, tab)
            }
        )
    }

    fun onTool(tool: ToolItem) {
        when (val action = tool.action) {
            is ToolAction.StudentTab -> pendingTab = action.tab
            is ToolAction.Route -> onOpenRoute(action.route)
            is ToolAction.External -> openCustomTab(context, action.url)
        }
    }

    val gridState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
    LazyVerticalGrid(
        state = gridState,
                columns = GridCells.Adaptive(minSize = 148.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Dimens.space,
            end = Dimens.space,
            bottom = Dimens.spaceXxl
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceMd),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceMd)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(Modifier.statusBarsPadding().padding(top = Dimens.space, bottom = Dimens.spaceSm)) {
                Text(
                    "Explore",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "Every JNTUH tool in one place",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            assistantTool.card(::onTool)
        }

        sectionHeader("Your results")
        items(resultTools, key = { it.title }) { tool -> tool.card(::onTool) }

        sectionHeader("Analysis")
        items(analysisTools, key = { it.title }) { tool -> tool.card(::onTool) }

        sectionHeader("Resources")
        items(resourceTools, key = { it.title }) { tool -> tool.card(::onTool) }
    }
        StatusBarScrim(brush = SolidColor(MaterialTheme.colorScheme.background))
    }
}

private fun androidx.compose.foundation.lazy.grid.LazyGridScope.sectionHeader(title: String) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        SectionHeader(
            title = title,
            modifier = Modifier.padding(top = Dimens.spaceSm)
        )
    }
}

@Composable
private fun ToolItem.card(onTool: (ToolItem) -> Unit) {
    val tool = this
    ToolCard(
        title = tool.title,
        subtitle = tool.subtitle,
        icon = tool.icon,
        accent = tool.accent,
        onClick = { onTool(tool) },
        modifier = Modifier.heightIn(min = 120.dp)
    )
}
