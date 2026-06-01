package com.example.wishlot.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.wishlot.data.Wish
import com.example.wishlot.ui.theme.Coral
import com.example.wishlot.ui.theme.Gold
import kotlin.math.min

@Composable
fun FortuneWheel(
    items: List<Wish>,
    winner: Wish,
    onSpinEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation = remember(winner.id) { Animatable(0f) }
    val segmentCount = min(items.size, 8).coerceAtLeast(1)
    val winnerIndex = items.take(segmentCount).indexOfFirst { it.id == winner.id }
        .let { if (it >= 0) it else 0 }

    LaunchedEffect(winner.id) {
        if (segmentCount > 1) {
            val segmentAngle = 360f / segmentCount
            val target = 360f * 4 + (360f - winnerIndex * segmentAngle - segmentAngle / 2f)
            rotation.animateTo(
                targetValue = target,
                animationSpec = tween(durationMillis = 2_500, easing = FastOutSlowInEasing),
            )
        }
        onSpinEnd()
    }

    val colors = listOf(Coral, Gold, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary)

    Canvas(
        modifier = modifier.size(220.dp),
    ) {
        val diameter = min(size.width, size.height)
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val arcSize = Size(diameter, diameter)

        rotate(rotation.value, pivot = center) {
            if (segmentCount == 1) {
                drawArc(
                    color = colors[0],
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize,
                )
            } else {
                val sweep = 360f / segmentCount
                repeat(segmentCount) { index ->
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = index * sweep - 90f,
                        sweepAngle = sweep,
                        useCenter = true,
                        topLeft = topLeft,
                        size = arcSize,
                    )
                }
            }
        }

        drawCircle(
            color = Color.White,
            radius = diameter * 0.08f,
            center = center,
        )
    }
}
