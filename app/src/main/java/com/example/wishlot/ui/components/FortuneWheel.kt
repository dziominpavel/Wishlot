package com.example.wishlot.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Box(
        modifier = modifier.size(240.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.wheel_disc),
            contentDescription = "Колесо фортуны, крутится",
            modifier = Modifier
                .size(224.dp)
                .clip(CircleShape)
                .graphicsLayer {
                    rotationZ = rotation.value
                },
        )
        Image(
            painter = painterResource(R.drawable.wheel_pointer),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(52.dp)
                .offset(y = (-8).dp),
        )
    }
}
