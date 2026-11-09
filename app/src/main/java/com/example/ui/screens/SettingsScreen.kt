package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    isSandboxActive: Boolean,
    onToggleSandbox: (Boolean) -> Unit,
    autoBlockEnabled: Boolean,
    onToggleAutoBlock: (Boolean) -> Unit,
    fraudThreshold: Float,
    onThresholdChange: (Float) -> Unit,
    onSimulateAttack: () -> Unit,
    onResetDemoData: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "CONFIGURATION & SYSTEM PREFERENCES",
                    color = GuardianEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Environment & Heuristics Settings",
                    color = GuardianTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Sandbox Environment Toggle
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
                    Text(
                        text = "ENVIRONMENT MODE",
                        color = GuardianCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PayPal Sandbox Gateway Mode",
                                color = GuardianTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Routes authorization checks to virtual test accounts. No live transactions.",
                                color = GuardianTextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Switch(
                            checked = isSandboxActive,
                            onCheckedChange = onToggleSandbox,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GuardianEmerald,
                                checkedTrackColor = GuardianEmeraldContainer
                            ),
                            modifier = Modifier.testTag("sandbox_toggle_switch")
                        )
                    }

                    HorizontalDivider(color = GuardianBorder, thickness = 0.5.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Autonomous Threat Blocking",
                                color = GuardianTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Instantly terminates payment sessions scoring above threshold.",
                                color = GuardianTextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Switch(
                            checked = autoBlockEnabled,
                            onCheckedChange = onToggleAutoBlock,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GuardianEmerald,
                                checkedTrackColor = GuardianEmeraldContainer
                            ),
                            modifier = Modifier.testTag("auto_block_toggle_switch")
                        )
                    }
                }
            }
        }

        // Risk Sensitivity
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fraud Interception Threshold",
                            color = GuardianTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${fraudThreshold.toInt()} pts",
                            color = GuardianAmber,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Slider(
                        value = fraudThreshold,
                        onValueChange = onThresholdChange,
                        valueRange = 30f..95f,
                        colors = SliderDefaults.colors(
                            thumbColor = GuardianEmerald,
                            activeTrackColor = GuardianEmerald,
                            inactiveTrackColor = GuardianSurfaceVariant
                        ),
                        modifier = Modifier.testTag("settings_threshold_slider")
                    )

                    Text(
                        text = "Threshold determines at what risk score incoming payments transition from review to automated quarantine.",
                        color = GuardianTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Test Tools & Simulation
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
                    Text(
                        text = "INTEGRATION SIMULATORS",
                        color = GuardianCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = onSimulateAttack,
                        colors = ButtonDefaults.buttonColors(containerColor = GuardianAmberContainer),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GuardianAmber),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_simulate_attack_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = GuardianAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Trigger Simulated Adversarial Attack", color = GuardianAmber, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = onResetDemoData,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GuardianRose),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GuardianRose),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reset_demo_data_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = GuardianRose, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset Demo Environment to Baseline", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // System Disclaimer & Hackathon Compliance Info
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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "LEGAL & PROTOTYPE DISCLAIMER",
                        color = GuardianTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "PAYPAL AI GUARDIAN is an independent cybersecurity hackathon prototype built for automated fraud prevention research. It is not an official PayPal product and is not officially affiliated with PayPal Inc. All transactions and entities in this demonstration are simulated for testing and evaluation purposes.",
                        color = GuardianTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "Build: v1.0.0-STAGE1 • Architecture: Clean MVVM Compose",
                        color = GuardianEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
