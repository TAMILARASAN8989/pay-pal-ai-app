package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FraudRule
import com.example.ui.theme.*

@Composable
fun FraudDetectionScreen(
    rules: List<FraudRule>,
    threshold: Float,
    onThresholdChange: (Float) -> Unit,
    onToggleRule: (String) -> Unit,
    onSimulateAttack: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("fraud_detection_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "FRAUD ENGINE & THREAT HEURISTICS",
                    color = GuardianEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Real-Time Defense Rules",
                    color = GuardianTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Configured anomaly detection algorithms monitoring incoming sandbox payments.",
                    color = GuardianTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Engine Status & Simulation Trigger Card
        item {
            Card(
                modifier = Modifier
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
                                text = "Engine Status: ACTIVE",
                                color = GuardianEmerald,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${rules.count { it.isEnabled }} of ${rules.size} heuristic models enabled",
                                color = GuardianTextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = onSimulateAttack,
                            colors = ButtonDefaults.buttonColors(containerColor = GuardianAmberContainer),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GuardianAmber),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("simulate_attack_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = GuardianAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Simulate Attack", color = GuardianAmber, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    HorizontalDivider(color = GuardianBorder, thickness = 0.5.dp)

                    // Fraud Sensitivity Slider
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Autonomous Blocking Sensitivity",
                                color = GuardianTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Score > ${threshold.toInt()}",
                                color = GuardianAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Slider(
                            value = threshold,
                            onValueChange = onThresholdChange,
                            valueRange = 30f..95f,
                            colors = SliderDefaults.colors(
                                thumbColor = GuardianEmerald,
                                activeTrackColor = GuardianEmerald,
                                inactiveTrackColor = GuardianSurfaceVariant
                            ),
                            modifier = Modifier.testTag("fraud_sensitivity_slider")
                        )

                        Text(
                            text = "Transactions scoring higher than ${threshold.toInt()} are intercepted and routed to sandbox quarantine.",
                            color = GuardianTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Active Rules Section
        item {
            Text(
                text = "HEURISTIC DETECTION RULES",
                color = GuardianTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        items(rules) { rule ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fraud_rule_${rule.id}")
                    .border(
                        1.dp,
                        if (rule.isEnabled) GuardianBorder else GuardianBorder.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (rule.isEnabled) GuardianDarkSurface else GuardianDarkBg
                ),
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = rule.name,
                                color = if (rule.isEnabled) GuardianTextPrimary else GuardianTextMuted,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Category: ${rule.category} • Weight: +${rule.riskWeight}pts",
                                color = GuardianCyan,
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = rule.isEnabled,
                            onCheckedChange = { onToggleRule(rule.id) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GuardianEmerald,
                                checkedTrackColor = GuardianEmeraldContainer,
                                uncheckedThumbColor = GuardianTextMuted,
                                uncheckedTrackColor = GuardianSurfaceVariant
                            ),
                            modifier = Modifier.testTag("switch_rule_${rule.id}")
                        )
                    }

                    Text(
                        text = rule.description,
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
                            text = "Triggers: ${rule.triggerCount} events",
                            color = GuardianTextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Last fired: ${rule.lastTriggered}",
                            color = GuardianTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
