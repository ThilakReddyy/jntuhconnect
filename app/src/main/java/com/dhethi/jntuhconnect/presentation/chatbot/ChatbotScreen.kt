package com.dhethi.jntuhconnect.presentation.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.EventNote
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.GradientCard
import com.dhethi.jntuhconnect.presentation.components.SectionHeader
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.glassOnBrand

/** A single thing the assistant will be able to answer, shown as an icon + copy row. */
private data class ChatSkill(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: Color
)

private val chatSkills = listOf(
    ChatSkill(
        "Academic results",
        "\"What's my CGPA?\" — instant marks & grades",
        Icons.Rounded.School, Color(0xFF0E9F6E)
    ),
    ChatSkill(
        "Backlogs",
        "\"Which subjects do I still need to clear?\"",
        Icons.Rounded.ErrorOutline, Color(0xFFE11D48)
    ),
    ChatSkill(
        "Syllabus",
        "\"Show me the units for DBMS this sem.\"",
        Icons.Rounded.MenuBook, Color(0xFF0E7C86)
    ),
    ChatSkill(
        "Academic calendars",
        "\"When does the next semester begin?\"",
        Icons.Rounded.CalendarMonth, Color(0xFF4F46E5)
    ),
    ChatSkill(
        "Exam details",
        "\"When are my end-sem exams scheduled?\"",
        Icons.Rounded.EventNote, Color(0xFFF59E0B)
    )
)

@Composable
fun ChatbotScreen(navigateBack: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar(title = "AI Assistant", onBack = navigateBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = Dimens.space)
                .padding(bottom = Dimens.spaceXxl),
            verticalArrangement = Arrangement.spacedBy(Dimens.space)
        ) {
            ChatbotHero()

            SectionHeader(title = "What you'll be able to ask")

            chatSkills.forEach { skill -> SkillRow(skill) }

            Text(
                text = "Ask anything about your academics in plain English — the assistant " +
                    "pulls it straight from your results, backlogs, syllabus, calendars and " +
                    "exam schedule. We're putting the finishing touches on it now.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Dimens.spaceXs)
            )
        }
    }
}

@Composable
private fun ChatbotHero() {
    GradientCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(Dimens.spaceXl)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(Dimens.heroRing.times(0.6f))
                        .clip(CircleShape)
                        .background(glassOnBrand),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SmartToy,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(Dimens.iconLg)
                    )
                }
                Spacer(Modifier.size(Dimens.space))
                ComingSoonBadge()
            }

            Spacer(Modifier.height(Dimens.spaceLg))

            Text(
                text = "Meet your JNTUH assistant",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(Dimens.spaceXs))
            Text(
                text = "A chat companion that answers everything about your academics — " +
                    "results, backlogs, syllabus, calendars and exams — in seconds.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun ComingSoonBadge() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(glassOnBrand)
            .padding(horizontal = Dimens.spaceMd, vertical = Dimens.spaceXs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXs)
    ) {
        Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(Dimens.iconSm)
        )
        Text(
            text = "COMING SOON",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun SkillRow(skill: ChatSkill) {
    AppCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(Dimens.space)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(Dimens.avatar)
                    .clip(RoundedCornerShape(Dimens.radiusMd))
                    .background(skill.accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = skill.icon,
                    contentDescription = null,
                    tint = skill.accent,
                    modifier = Modifier.size(Dimens.icon)
                )
            }
            Spacer(Modifier.size(Dimens.space))
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = skill.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = skill.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
