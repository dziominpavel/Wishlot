package com.example.wishlot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.wishlot.ui.navigation.AppDestination

enum class AppDestinations(
    override val label: String,
    override val icon: ImageVector,
) : AppDestination {
    WISHLIST("Вишлист", Icons.Filled.CardGiftcard),
    TREAT("Крутить", Icons.Filled.Celebration),
    SETTINGS("Настройки", Icons.Filled.Settings),
}
