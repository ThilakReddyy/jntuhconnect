package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import java.util.Locale
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.Shape

const val ROLL_NUMBER_LENGTH = 10

fun normalizeRollNumber(input: String): String = input
    .filter(Char::isLetterOrDigit)
    .uppercase(Locale.ROOT)
    .take(ROLL_NUMBER_LENGTH)

fun isValidRollNumber(value: String): Boolean =
    value.length == ROLL_NUMBER_LENGTH && value.all(Char::isLetterOrDigit)

/**
 * Premium roll-number input. Auto-uppercases, caps at 10 chars, shows a leading badge
 * icon and (optionally) a trailing search action that fires when [onSubmit] is set.
 */
@Composable
fun RollNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Roll Number",
    placeholder: String = "Enter 10-character roll number",
    imeAction: ImeAction = ImeAction.Search,
    onSubmit: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null,
    showSearchAction: Boolean = true,
    error: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(normalizeRollNumber(it)) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = Shape,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        isError = error != null,
        supportingText = error?.let { message -> { Text(message) } },
        leadingIcon = {
            Icon(Icons.Rounded.Badge, contentDescription = null)
        },
        trailingIcon = if (showSearchAction && onSubmit != null) {
            {
                IconButton(onClick = onSubmit) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.icon)
                    )
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSubmit?.invoke() },
            onDone = { onSubmit?.invoke() },
            onNext = { onNext?.invoke() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}
