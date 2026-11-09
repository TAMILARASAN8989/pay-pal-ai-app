package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionStatus
import com.example.ui.theme.*

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    testTag: String = "kpi_card"
) {
    Card(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .border(1.dp, GuardianBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    color = GuardianTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = value,
                color = GuardianTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Text(
                    text = subtitle,
                    color = GuardianTextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun RiskBadge(
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier
) {
    val (bg, fg, border) = when (riskLevel) {
        RiskLevel.LOW -> Triple(
            GuardianEmerald.copy(alpha = 0.12f),
            GuardianEmerald,
            GuardianEmerald.copy(alpha = 0.35f)
        )
        RiskLevel.MEDIUM -> Triple(
            GuardianAmber.copy(alpha = 0.12f),
            GuardianAmber,
            GuardianAmber.copy(alpha = 0.35f)
        )
        RiskLevel.HIGH -> Triple(
            GuardianRose.copy(alpha = 0.12f),
            GuardianRose,
            GuardianRose.copy(alpha = 0.35f)
        )
        RiskLevel.CRITICAL -> Triple(
            Color(0xFFE11D48).copy(alpha = 0.16f),
            Color(0xFFFB7185),
            Color(0xFFE11D48).copy(alpha = 0.45f)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(fg)
            )
            Text(
                text = riskLevel.label,
                color = fg,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: TransactionStatus,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (status) {
        TransactionStatus.VERIFIED -> Pair(GuardianEmerald.copy(alpha = 0.15f), GuardianEmerald)
        TransactionStatus.PENDING -> Pair(GuardianAmber.copy(alpha = 0.15f), GuardianAmber)
        TransactionStatus.FLAGGED -> Pair(GuardianRose.copy(alpha = 0.15f), GuardianRose)
        TransactionStatus.BLOCKED -> Pair(Color(0xFF881337).copy(alpha = 0.3f), Color(0xFFFDA4AF))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 7.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.label,
            color = fg,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    action: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                color = GuardianTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = GuardianTextSecondary,
                    fontSize = 12.sp
                )
            }
        }
        if (action != null) {
            action()
        }
    }
}
