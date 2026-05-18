package com.example.wishlot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wishlot.R
import com.example.wishlot.data.Wish
import com.example.wishlot.ui.components.WishCard
import com.example.wishlot.ui.theme.Spacing

@Composable
fun WishlistScreen(
    wishes: List<Wish>,
    formatPrice: (Long) -> String,
    onWishClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onMoveUp: (Long) -> Unit,
    onMoveDown: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.wishlist_add))
            }
        },
    ) { padding ->
        if (wishes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(Spacing.lg),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.wishlist_empty),
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
                item { Spacer(modifier = Modifier.padding(top = Spacing.xs)) }
                itemsIndexed(wishes, key = { _, wish -> wish.id }) { index, wish ->
                    WishCard(
                        wish = wish,
                        priceLabel = formatPrice(wish.priceMinor),
                        onClick = { onWishClick(wish.id) },
                        onMoveUp = if (index > 0) {{ onMoveUp(wish.id) }} else null,
                        onMoveDown = if (index < wishes.lastIndex) {{ onMoveDown(wish.id) }} else null,
                    )
                }
                item { Spacer(modifier = Modifier.padding(bottom = Spacing.lg + Spacing.md)) }
            }
        }
    }
}
