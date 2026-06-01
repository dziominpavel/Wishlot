package com.example.wishlot.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.example.wishlot.R
import com.example.wishlot.data.Wish
import com.example.wishlot.ui.components.FortuneWheel
import com.example.wishlot.ui.theme.Spacing
import com.example.wishlot.viewmodel.PickUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinResultScreen(
    pickState: PickUiState,
    formatPrice: (Long) -> String,
    onSpinAnimationComplete: () -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onSpinAgain: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    var hapticDone by remember { mutableStateOf(false) }

    val winner = when (pickState) {
        is PickUiState.Spinning -> pickState.winner
        is PickUiState.AwaitingDecision -> pickState.winner
        else -> return
    }
    val wheelItems = when (pickState) {
        is PickUiState.Spinning -> pickState.wheelItems
        is PickUiState.AwaitingDecision -> listOf(pickState.winner)
        else -> emptyList()
    }
    val showActions = pickState is PickUiState.AwaitingDecision

    LaunchedEffect(showActions) {
        if (showActions && !hapticDone) {
            view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            hapticDone = true
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.spin_result_title)) },
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
                .padding(horizontal = Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (pickState is PickUiState.Spinning) {
                FortuneWheel(
                    items = wheelItems,
                    winner = winner,
                    onSpinEnd = onSpinAnimationComplete,
                )
            }
            Spacer(modifier = Modifier.height(Spacing.md))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(Spacing.md)) {
                    Text(
                        text = winner.title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = formatPrice(winner.priceMinor),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = Spacing.xs),
                    )
                    winner.note?.takeIf { it.isNotBlank() }?.let { note ->
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = Spacing.xs),
                        )
                    }
                }
            }
            if (showActions) {
                Text(
                    text = stringResource(R.string.spin_fulfilled_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = Spacing.md),
                )
                Spacer(modifier = Modifier.height(Spacing.md))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.spin_decline))
                    }
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.spin_accept))
                    }
                }
                OutlinedButton(
                    onClick = onSpinAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.xs),
                ) {
                    Text(stringResource(R.string.spin_again))
                }
            }
        }
    }
    }
}

@Composable
fun NoCandidatesScreen(
    onChangeBudget: () -> Unit,
    onGoWishlist: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.pick_no_candidates_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(R.string.pick_no_candidates_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = Spacing.md),
        )
        Spacer(modifier = Modifier.height(Spacing.lg))
        Button(onClick = onChangeBudget, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.pick_change_budget))
        }
        OutlinedButton(
            onClick = onGoWishlist,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.xs),
        ) {
            Text(stringResource(R.string.pick_go_wishlist))
        }
    }
    }
}
