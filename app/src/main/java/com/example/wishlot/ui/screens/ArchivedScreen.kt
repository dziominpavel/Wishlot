package com.example.wishlot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wishlot.R
import com.example.wishlot.data.Wish
import com.example.wishlot.ui.components.CategoryLabel
import com.example.wishlot.ui.theme.Spacing
import com.example.wishlot.data.WishCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivedScreen(
    wishes: List<Wish>,
    formatPrice: (Long) -> String,
    onBack: () -> Unit,
    onRestore: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.archived_title)) },
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
        if (wishes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(Spacing.lg),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.archived_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                items(wishes, key = { it.id }) { wish ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(Spacing.md)) {
                            CategoryLabel(category = WishCategory.fromName(wish.category))
                            Text(
                                text = wish.title,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = Spacing.xs),
                            )
                            Text(
                                text = formatPrice(wish.priceMinor),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = Spacing.xs),
                            )
                            OutlinedButton(
                                onClick = { onRestore(wish.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = Spacing.md),
                            ) {
                                Text(stringResource(R.string.restore_to_wishlist))
                            }
                            OutlinedButton(
                                onClick = { onDelete(wish.id) },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.wish_delete))
                            }
                        }
                    }
                }
            }
        }
    }
}
