package com.amrubio27.compose_superheroes.features.detail.presentation.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.amrubio27.compose_superheroes.features.detail.presentation.PowerStatsUi
import com.amrubio27.compose_superheroes.ui.theme.dimens
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChartContainer(stats: PowerStatsUi) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(MaterialTheme.dimens.paddingMedium),
        contentAlignment = Alignment.Center
    ) {
        RadarChart(
            data = mapOf(
                "INT" to stats.intelligence,
                "STR" to stats.strength,
                "SPD" to stats.speed,
                "DUR" to stats.durability,
                "POW" to stats.power,
                "CMB" to stats.combat
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun RadarChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
    labelColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val labels = data.keys.toList()
    val values = data.values.toList()
    val maxValue = 100f

    // Use LocalDensity to convert Sp to Px for text size
    val density = LocalDensity.current
    val labelTextSize = with(density) { 12.sp.toPx() }

    val webColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (minOf(size.width, size.height) / 2) * 0.8f
        val angleStep = (2 * Math.PI / labels.size).toFloat()

        // Draw Web
        for (i in 1..4) {
            val r = radius * (i / 4f)
            drawPath(
                path = Path().apply {
                    for (j in labels.indices) {
                        val angle = j * angleStep - Math.PI.toFloat() / 2
                        val x = center.x + r * cos(angle)
                        val y = center.y + r * sin(angle)
                        if (j == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                },
                color = webColor,
                style = Stroke(width = 2f)
            )
        }

        // Draw Axes
        for (i in labels.indices) {
            val angle = i * angleStep - Math.PI.toFloat() / 2
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)
            drawLine(
                color = webColor,
                start = center,
                end = Offset(x, y),
                strokeWidth = 2f
            )

            // Draw Labels
            val labelRadius = radius * 1.15f
            val labelX = center.x + labelRadius * cos(angle)
            val labelY = center.y + labelRadius * sin(angle)

            drawContext.canvas.nativeCanvas.drawText(
                labels[i],
                labelX,
                labelY,
                Paint().apply {
                    color = labelColor.toArgb()
                    textSize = labelTextSize
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }

        // Draw Data Polygon
        val path = Path()
        for (i in values.indices) {
            val value = values[i] / maxValue
            val r = radius * value
            val angle = i * angleStep - Math.PI.toFloat() / 2
            val x = center.x + r * cos(angle)
            val y = center.y + r * sin(angle)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()

        drawPath(path = path, color = fillColor)
        drawPath(path = path, color = lineColor, style = Stroke(width = 4f, cap = StrokeCap.Round))
    }
}
