package com.example.wishlot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wishlot.R
import com.example.wishlot.data.Wish
import com.example.wishlot.ui.components.CategoryLabel
import com.example.wishlot.data.WishCategory
import com.example.wishlot.ui.theme.Spacing
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    wishes: List<Wish>,
    formatPrice: (Long) -> String,
    onRestore: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val zone = ZoneId.systemDefault()

    if (wishes.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(Spacing.lg),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.history_empty),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(Spacing.md),
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
                        wish.fulfilledAt?.let { at ->
                            val formatted = Instant.ofEpochMilli(at)
                                .atZone(zone)
                                .format(dateFormatter)
                            Text(
                                text = stringResource(R.string.history_fulfilled_at, formatted),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = Spacing.xs),
                            )
                        }
                        OutlinedButton(
                            onClick = { onRestore(wish.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Spacing.md),
                        ) {
                            Text(stringResource(R.string.restore_to_wishlist))
                        }
                    }
                }
            }
        }
    }
}
