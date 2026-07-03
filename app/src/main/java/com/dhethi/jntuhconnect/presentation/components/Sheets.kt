package com.dhethi.jntuhconnect.presentation.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.dhethi.jntuhconnect.presentation.theme.Dimens

/**
 * Bottom sheet that collects a single roll number then invokes [onSubmit]. Used by the
 * Explore tools that need a roll number (Academic/All/Backlog/Credits shortcuts, etc.).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollInputSheet(
    title: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var roll by remember { mutableStateOf("") }
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = Dimens.spaceLg)
                .padding(bottom = Dimens.spaceLg)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(Dimens.space))
            RollNumberField(
                value = roll,
                onValueChange = { roll = it },
                showSearchAction = false
            )
            Spacer(Modifier.height(Dimens.space))
            PrimaryButton(
                text = "Continue",
                onClick = {
                    if (roll.length != ROLL_NUMBER_LENGTH) {
                        Toast.makeText(
                            context,
                            "Roll number must be exactly 10 characters",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onSubmit(roll)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Generic single-select bottom sheet picker (degree / year / regulation / type, etc.).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerSheet(
    title: String,
    options: List<String>,
    selected: String?,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = Dimens.spaceLg)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = Dimens.spaceLg)
            )
            Spacer(Modifier.height(Dimens.spaceMd))
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(option) }
                        .padding(horizontal = Dimens.spaceLg, vertical = Dimens.space),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (option == selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (option == selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                    if (option == selected) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
