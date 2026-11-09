package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.data.model.SecurityAlert
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*

@Composable
fun AlertsScreen(
    alerts: List<SecurityAlert>,
    onResolveAlert: (String) -> Unit,
    onDismissAlert: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedSeverity by remember { mutableStateOf<RiskLevel?>(null) }

    val filteredAlerts = alerts.filter { alert ->
        selectedSeverity == null || alert.severity == selectedSeverity
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("alerts_screen")
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SECURITY THREAT ALERTS",
                    color = GuardianRose,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Active Incident Feed",
                    color = GuardianTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${filteredAlerts.size} active",
                color = GuardianTextSecondary,
                fontSize = 12.sp
            )
        }

        // Severity Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedSeverity == null,
                onClick = { selectedSeverity = null },
                label = { Text("All Severities", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GuardianDarkSurface,
                    selectedLabelColor = GuardianEmerald,
                    containerColor = GuardianDarkBg,
                    labelColor = GuardianTextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (selectedSeverity == null) GuardianEmerald else GuardianBorder,
                    enabled = true,
                    selected = selectedSeverity == null
                )
            )

            listOf(RiskLevel.CRITICAL, RiskLevel.HIGH, RiskLevel.MEDIUM, RiskLevel.LOW).forEach { level ->
                val isSelected = selectedSeverity == level
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedSeverity = if (isSelected) null else level },
                    label = { Text(level.label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GuardianDarkSurface,
                        selectedLabelColor = GuardianEmerald,
                        containerColor = GuardianDarkBg,
                        labelColor = GuardianTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) GuardianEmerald else GuardianBorder,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        // Alerts List or Empty
        if (filteredAlerts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GuardianBorder),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GuardianEmerald,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No Threat Alerts Pending",
                            color = GuardianTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "All security incidents have been addressed or filtered.",
                            color = GuardianTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredAlerts) { alert ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("alert_card_${alert.id}")
                            .border(
                                1.dp,
                                if (alert.isResolved) GuardianBorder else when (alert.severity) {
                                    RiskLevel.CRITICAL, RiskLevel.HIGH -> GuardianRose.copy(alpha = 0.5f)
                                    RiskLevel.MEDIUM -> GuardianAmber.copy(alpha = 0.5f)
                                    RiskLevel.LOW -> GuardianEmerald.copy(alpha = 0.5f)
                                },
                                RoundedCornerShape(12.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    RiskBadge(riskLevel = alert.severity)
                                    Text(
                                        text = alert.category,
                                        color = GuardianCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = alert.timestamp,
                                    color = GuardianTextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Text(
                                text = alert.title,
                                color = GuardianTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = alert.description,
                                color = GuardianTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Target: ${alert.affectedRef}",
                                    color = GuardianTextMuted,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (!alert.isResolved) {
                                        Button(
                                            onClick = { onResolveAlert(alert.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = GuardianEmeraldContainer),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            modifier = Modifier.testTag("resolve_alert_${alert.id}")
                                        ) {
                                            Text("Mitigate", color = GuardianEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Text(
                                            text = "Mitigated ✓",
                                            color = GuardianEmerald,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    IconButton(
                                        onClick = { onDismissAlert(alert.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss",
                                            tint = GuardianTextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
