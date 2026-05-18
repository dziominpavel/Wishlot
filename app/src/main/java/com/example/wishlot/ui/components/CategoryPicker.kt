package com.example.wishlot.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wishlot.data.WishCategory
import com.example.wishlot.ui.theme.Spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryPicker(
    selected: WishCategory,
    onSelected: (WishCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        WishCategory.entries.forEach { category ->
            FilterChip(
                selected = category == selected,
                onClick = { onSelected(category) },
                label = { Text(stringResource(category.labelRes)) },
                leadingIcon = {
                    Canvas(modifier = Modifier.size(Spacing.md)) {
                        drawCircle(color = category.color)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PriorityPicker(
    selected: com.example.wishlot.data.WishPriority,
    onSelected: (com.example.wishlot.data.WishPriority) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        com.example.wishlot.data.WishPriority.entries.forEach { priority ->
            FilterChip(
                selected = priority == selected,
                onClick = { onSelected(priority) },
                label = { Text(stringResource(priority.labelRes)) },
            )
        }
    }
}

@Composable
fun CategoryLabel(
    category: WishCategory,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(category.labelRes),
        style = MaterialTheme.typography.labelSmall,
        color = category.color,
        modifier = modifier,
    )
}
