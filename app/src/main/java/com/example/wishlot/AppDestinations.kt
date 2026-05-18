package com.example.wishlot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.wishlot.ui.navigation.AppDestination

enum class AppDestinations(
    override val label: String,
    override val icon: ImageVector,
) : AppDestination {
    WISHLIST("Вишлист", Icons.AutoMirrored.Filled.List),
    TREAT("Побаловать себя", Icons.Default.Casino),
    HISTORY("История", Icons.Default.History),
    SETTINGS("Настройки", Icons.Default.Settings),
}
