package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityChartPoint
import com.example.ui.theme.*

@Composable
fun ActivityTrendChart(
    points: List<ActivityChartPoint>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GuardianBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Transaction Activity & Threats",
                        color = GuardianTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Real-time 7-day sandbox event volume",
                        color = GuardianTextSecondary,
                        fontSize = 12.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GuardianCyan)
                        )
                        Text("Volume", color = GuardianTextSecondary, fontSize = 11.sp)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GuardianRose)
                        )
                        Text("Threats", color = GuardianTextSecondary, fontSize = 11.sp)
                    }
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                if (points.isEmpty()) return@Canvas
                val width = size.width
                val height = size.height
                val paddingBottom = 20.dp.toPx()
                val graphHeight = height - paddingBottom
                val maxVal = (points.maxOfOrNull { it.volume } ?: 100f).coerceAtLeast(10f)

                // Draw horizontal guide lines
                val gridLines = 3
                for (i in 0..gridLines) {
                    val y = (graphHeight / gridLines) * i
                    drawLine(
                        color = GuardianBorder.copy(alpha = 0.5f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                    )
                }

                val stepX = width / (points.size - 1).coerceAtLeast(1)

                // Volume path (Cyan)
                val volumePath = Path()
                val fillPath = Path()
                points.forEachIndexed { index, pt ->
                    val x = index * stepX
                    val y = graphHeight - (pt.volume / maxVal) * (graphHeight * 0.85f)
                    if (index == 0) {
                        volumePath.moveTo(x, y)
                        fillPath.moveTo(x, graphHeight)
                        fillPath.lineTo(x, y)
                    } else {
                        volumePath.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }
                fillPath.lineTo(width, graphHeight)
                fillPath.close()

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(GuardianCyan.copy(alpha = 0.25f), Color.Transparent),
                        startY = 0f,
                        endY = graphHeight
                    )
                )

                drawPath(
                    path = volumePath,
                    color = GuardianCyan,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Threat Line (Rose)
                val threatPath = Path()
                points.forEachIndexed { index, pt ->
                    val x = index * stepX
                    val y = graphHeight - (pt.flagCount / maxVal) * (graphHeight * 0.85f)
                    if (index == 0) threatPath.moveTo(x, y) else threatPath.lineTo(x, y)

                    // Draw dots
                    drawCircle(
                        color = GuardianRose,
                        radius = 3.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
                drawPath(
                    path = threatPath,
                    color = GuardianRose,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Labels under graph
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                points.forEach { pt ->
                    Text(
                        text = pt.day,
                        color = GuardianTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun RiskDistributionDonutChart(
    lowCount: Int,
    mediumCount: Int,
    highCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GuardianBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column {
                Text(
                    text = "Risk Severity Distribution",
                    color = GuardianTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Classification of current transaction flow",
                    color = GuardianTextSecondary,
                    fontSize = 12.sp
                )
            }

            val total = (lowCount + mediumCount + highCount).coerceAtLeast(1)
            val lowAngle = (lowCount.toFloat() / total) * 360f
            val mediumAngle = (mediumCount.toFloat() / total) * 360f
            val highAngle = (highCount.toFloat() / total) * 360f

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 14.dp.toPx()
                        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                        val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                        var startAngle = -90f
                        // Low (Emerald)
                        drawArc(
                            color = GuardianEmerald,
                            startAngle = startAngle,
                            sweepAngle = lowAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        startAngle += lowAngle

                        // Medium (Amber)
                        drawArc(
                            color = GuardianAmber,
                            startAngle = startAngle,
                            sweepAngle = mediumAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        startAngle += mediumAngle

                        // High (Rose)
                        drawArc(
                            color = GuardianRose,
                            startAngle = startAngle,
                            sweepAngle = highAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$total",
                            color = GuardianTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Audited",
                            color = GuardianTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LegendItem(
                        label = "Low Risk",
                        count = lowCount,
                        percent = ((lowCount.toFloat() / total) * 100).toInt(),
                        color = GuardianEmerald
                    )
                    LegendItem(
                        label = "Medium Risk",
                        count = mediumCount,
                        percent = ((mediumCount.toFloat() / total) * 100).toInt(),
                        color = GuardianAmber
                    )
                    LegendItem(
                        label = "High / Critical",
                        count = highCount,
                        percent = ((highCount.toFloat() / total) * 100).toInt(),
                        color = GuardianRose
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(
    label: String,
    count: Int,
    percent: Int,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = "$label: $count ($percent%)",
            color = GuardianTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
