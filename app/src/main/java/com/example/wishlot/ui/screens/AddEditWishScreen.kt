package com.example.wishlot.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.wishlot.R
import com.example.wishlot.ui.components.CategoryPicker
import com.example.wishlot.ui.components.PriorityPicker
import com.example.wishlot.ui.theme.Spacing
import com.example.wishlot.viewmodel.WishEditDraft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWishScreen(
    draft: WishEditDraft,
    onBack: () -> Unit,
    onSave: (WishEditDraft) -> Unit,
    onDelete: ((Long) -> Unit)?,
    onArchive: ((Long) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    var title by remember(draft) { mutableStateOf(draft.title) }
    var priceInput by remember(draft) { mutableStateOf(draft.priceInput) }
    var note by remember(draft) { mutableStateOf(draft.note) }
    var category by remember(draft) { mutableStateOf(draft.category) }
    var priority by remember(draft) { mutableStateOf(draft.priority) }

    val titleRes = if (draft.id != null) R.string.wish_edit_title else R.string.wish_add_title

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(titleRes)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.confirm_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Spacing.md)
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.wish_title_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            OutlinedTextField(
                value = priceInput,
                onValueChange = { priceInput = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                label = { Text(stringResource(R.string.wish_price_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = stringResource(R.string.wish_category_label),
                style = MaterialTheme.typography.labelLarge,
            )
            CategoryPicker(
                selected = category,
                onSelected = { category = it },
                modifier = Modifier.padding(top = Spacing.xs),
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = stringResource(R.string.wish_priority_label),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = stringResource(R.string.wish_priority_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            PriorityPicker(
                selected = priority,
                onSelected = { priority = it },
                modifier = Modifier.padding(top = Spacing.xs),
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text(stringResource(R.string.wish_note_label)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )
            Spacer(modifier = Modifier.height(Spacing.lg))
            Button(
                onClick = {
                    onSave(
                        draft.copy(
                            title = title,
                            priceInput = priceInput,
                            note = note,
                            category = category,
                            priority = priority,
                        ),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.wish_save))
            }
            if (draft.id != null) {
                if (onArchive != null) {
                    Spacer(modifier = Modifier.height(Spacing.md))
                    OutlinedButton(
                        onClick = { onArchive(draft.id) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.wish_archive))
                    }
                }
                if (onDelete != null) {
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    OutlinedButton(
                        onClick = { onDelete(draft.id) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.wish_delete))
                    }
                }
            }
        }
    }
}
