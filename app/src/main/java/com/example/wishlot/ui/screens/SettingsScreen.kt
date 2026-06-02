package com.example.wishlot.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wishlot.R
import com.example.wishlot.ui.theme.Spacing
import com.example.wishlot.viewmodel.WishStats

@Composable
fun SettingsScreen(
    stats: WishStats,
    formatPrice: (Long) -> String,
    onOpenHistory: () -> Unit,
    versionName: String,
    buildDate: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.lg),
    ) {
        Text(
            text = stringResource(R.string.settings_stats_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = stringResource(
                R.string.settings_stats_active,
                stats.activeCount,
                formatPrice(stats.activeTotalMinor),
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = Spacing.xs),
        )
        Text(
            text = stringResource(
                R.string.settings_stats_fulfilled,
                stats.fulfilledCount,
                formatPrice(stats.fulfilledTotalMinor),
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
        if (stats.archivedCount > 0) {
            Text(
                text = stringResource(R.string.settings_stats_archived, stats.archivedCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.lg))

        OutlinedButton(
            onClick = onOpenHistory,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.history_title))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.lg))

        Text(
            text = stringResource(R.string.settings_version, versionName, buildDate),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
