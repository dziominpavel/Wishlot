package com.example.wishlot.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.wishlot.R
import com.example.wishlot.ui.theme.Spacing

@Composable
fun TreatYourselfScreen(
    budgetInput: String,
    onBudgetChange: (String) -> Unit,
    hasActiveWishes: Boolean,
    onSpin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.lg),
    ) {
        Text(
            text = stringResource(R.string.treat_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.treat_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = Spacing.xs),
        )
        Spacer(modifier = Modifier.height(Spacing.lg))
        OutlinedTextField(
            value = budgetInput,
            onValueChange = { onBudgetChange(it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }) },
            label = { Text(stringResource(R.string.treat_budget_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )
        if (!hasActiveWishes) {
            Text(
                text = stringResource(R.string.treat_no_active),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = Spacing.xs),
            )
        }
        Spacer(modifier = Modifier.height(Spacing.lg))
        Button(
            onClick = onSpin,
            modifier = Modifier.fillMaxWidth(),
            enabled = hasActiveWishes,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Casino, contentDescription = null)
                Spacer(modifier = Modifier.width(Spacing.xs))
                Text(stringResource(R.string.treat_spin))
            }
        }
    }
}
