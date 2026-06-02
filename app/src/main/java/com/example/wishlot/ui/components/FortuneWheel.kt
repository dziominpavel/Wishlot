package com.example.wishlot.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.wishlot.R

@Composable
fun FortuneWheel(
    spinKey: Any,
    onSpinEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation = remember(spinKey) { Animatable(0f) }

    LaunchedEffect(spinKey) {
        val fullSpins = (4..6).random()
        val finalAngle = (0..359).random().toFloat()
        val target = 360f * fullSpins + finalAngle
        rotation.animateTo(
            targetValue = target,
            animationSpec = tween(durationMillis = 3_000, easing = FastOutSlowInEasing),
        )
        onSpinEnd()
    }

    Image(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = "Колесо выбора, крутится",
        modifier = modifier
            .size(220.dp)
            .graphicsLayer {
                rotationZ = rotation.value
            },
    )
}
