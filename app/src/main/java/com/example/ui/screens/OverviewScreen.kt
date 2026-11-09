package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OverviewScreen(
    kpiMetrics: KpiMetrics,
    chartPoints: List<ActivityChartPoint>,
    recentTransactions: List<Transaction>,
    recentAlerts: List<SecurityAlert>,
    onSelectTransaction: (Transaction) -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onSimulateAttack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("overview_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome & Environment Status Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GuardianBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "SECURITY OPERATIONS CENTER",
                            color = GuardianEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Intelligent Payment Protection",
                            color = GuardianTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Real-time threat heuristics, anomaly detection, and sandbox verification feed.",
                            color = GuardianTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onSimulateAttack,
                        colors = ButtonDefaults.buttonColors(containerColor = GuardianAmberContainer),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GuardianAmber),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("overview_simulate_attack_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = GuardianAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Test Probe",
                                color = GuardianAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // KPI Cards Grid (2x2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "Total Transactions",
                        value = "${kpiMetrics.totalTransactions}",
                        subtitle = "Vol: $${String.format("%,.0f", kpiMetrics.totalVolumeUsd)}",
                        icon = Icons.Default.ReceiptLong,
                        accentColor = GuardianCyan,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_total_transactions"
                    )
                    KpiCard(
                        title = "Verified Sandbox",
                        value = "${kpiMetrics.verifiedPayments}",
                        subtitle = "Integrity 100%",
                        icon = Icons.Default.CheckCircle,
                        accentColor = GuardianEmerald,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_verified_payments"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "Suspicious Threats",
                        value = "${kpiMetrics.suspiciousCount}",
                        subtitle = "Prevented: $${String.format("%,.0f", kpiMetrics.preventedFraudUsd)}",
                        icon = Icons.Default.Warning,
                        accentColor = GuardianRose,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_suspicious_threats"
                    )
                    KpiCard(
                        title = "Requires Review",
                        value = "${kpiMetrics.reviewRequiredCount}",
                        subtitle = "Secondary auth",
                        icon = Icons.Default.PendingActions,
                        accentColor = GuardianAmber,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_requires_review"
                    )
                }
            }
        }

        // Charts Section: Activity Trend and Risk Severity Donut
        item {
            ActivityTrendChart(points = chartPoints)
        }

        item {
            val low = recentTransactions.count { it.riskLevel == RiskLevel.LOW }
            val med = recentTransactions.count { it.riskLevel == RiskLevel.MEDIUM }
            val high = recentTransactions.count { it.riskLevel == RiskLevel.HIGH || it.riskLevel == RiskLevel.CRITICAL }
            RiskDistributionDonutChart(lowCount = low, mediumCount = med, highCount = high)
        }

        // Recent Security Alerts
        item {
            SectionHeader(
                title = "Security Threat Activity",
                subtitle = "Active intrusion and fraud anomalies",
                action = {
                    TextButton(onClick = onNavigateToAlerts) {
                        Text("View All", color = GuardianEmerald, fontSize = 12.sp)
                    }
                }
            )
        }

        items(recentAlerts.take(3)) { alert ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GuardianBorder, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToAlerts() },
                colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when (alert.severity) {
                                    RiskLevel.LOW -> GuardianEmerald.copy(alpha = 0.15f)
                                    RiskLevel.MEDIUM -> GuardianAmber.copy(alpha = 0.15f)
                                    RiskLevel.HIGH, RiskLevel.CRITICAL -> GuardianRose.copy(alpha = 0.15f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = when (alert.severity) {
                                RiskLevel.LOW -> GuardianEmerald
                                RiskLevel.MEDIUM -> GuardianAmber
                                RiskLevel.HIGH, RiskLevel.CRITICAL -> GuardianRose
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = alert.title,
                                color = GuardianTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = alert.timestamp,
                                color = GuardianTextMuted,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = alert.description,
                            color = GuardianTextSecondary,
                            fontSize = 12.sp
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ref: ${alert.affectedRef}",
                                color = GuardianCyan,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            RiskBadge(riskLevel = alert.severity)
                        }
                    }
                }
            }
        }

        // Recent Transactions Section
        item {
            SectionHeader(
                title = "Recent Transactions",
                subtitle = "Monitored sandbox payment flow",
                action = {
                    TextButton(onClick = onNavigateToTransactions) {
                        Text("View All", color = GuardianEmerald, fontSize = 12.sp)
                    }
                }
            )
        }

        items(recentTransactions.take(5)) { txn ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("overview_txn_${txn.id}")
                    .border(1.dp, GuardianBorder, RoundedCornerShape(12.dp))
                    .clickable { onSelectTransaction(txn) },
                colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = txn.reference,
                                color = GuardianTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            StatusBadge(status = txn.status)
                        }
                        Text(
                            text = "${txn.senderName} → ${txn.recipientName}",
                            color = GuardianTextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = txn.timestamp,
                            color = GuardianTextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "$${String.format("%.2f", txn.amount)}",
                            color = GuardianTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        RiskBadge(riskLevel = txn.riskLevel)
                    }
                }
            }
        }
    }
}
