package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.RiskLevel
import com.example.data.model.Transaction
import com.example.ui.theme.*

@Composable
fun TransactionDetailDialog(
    transaction: Transaction,
    onDismiss: () -> Unit,
    onApprove: (Transaction) -> Unit,
    onBlock: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag("transaction_detail_dialog")
                .border(1.dp, GuardianBorder, RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = GuardianDarkBg),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TRANSACTION FORENSICS",
                            color = GuardianTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = transaction.reference,
                            color = GuardianTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_detail_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = GuardianTextSecondary
                        )
                    }
                }

                HorizontalDivider(color = GuardianBorder, thickness = 1.dp)

                // Risk Score Gauge Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (transaction.riskLevel) {
                            RiskLevel.LOW -> GuardianEmeraldContainer.copy(alpha = 0.4f)
                            RiskLevel.MEDIUM -> GuardianAmberContainer.copy(alpha = 0.4f)
                            RiskLevel.HIGH, RiskLevel.CRITICAL -> GuardianRoseContainer.copy(alpha = 0.4f)
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        when (transaction.riskLevel) {
                            RiskLevel.LOW -> GuardianEmerald
                            RiskLevel.MEDIUM -> GuardianAmber
                            RiskLevel.HIGH, RiskLevel.CRITICAL -> GuardianRose
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Risk Assessment Score",
                                color = GuardianTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${transaction.riskScore}/100",
                                color = GuardianTextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        RiskBadge(riskLevel = transaction.riskLevel)
                    }
                }

                // Core Metadata Grid
                Card(
                    colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GuardianBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetadataRow("Amount", "$${String.format("%.2f", transaction.amount)} ${transaction.currency}")
                        MetadataRow("Status", transaction.status.label)
                        MetadataRow("Timestamp", transaction.timestamp)
                        MetadataRow("Payment Method", transaction.paymentMethod)
                        MetadataRow("Sender", "${transaction.senderName} (${transaction.senderEmail})")
                        MetadataRow("Recipient", "${transaction.recipientName} (${transaction.recipientEmail})")
                    }
                }

                // Telemetry & Cyber Data
                Card(
                    colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GuardianBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "NETWORK & DEVICE TELEMETRY",
                            color = GuardianCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        MetadataRow("Origin IP", transaction.ipAddress)
                        MetadataRow("Geo Location", transaction.location)
                        MetadataRow("Device Fingerprint", transaction.deviceFingerprint)
                    }
                }

                // Fraud Signals / Anomaly list
                if (transaction.fraudSignals.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GuardianRose.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = GuardianRose,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "IDENTIFIED THREAT SIGNALS",
                                    color = GuardianRose,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            transaction.fraudSignals.forEach { signal ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 5.dp)
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(GuardianRose)
                                    )
                                    Text(
                                        text = signal,
                                        color = GuardianTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onApprove(transaction)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("approve_transaction_btn"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GuardianEmerald),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GuardianEmerald),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Approve", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            onBlock(transaction)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("block_transaction_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = GuardianRose),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Block & Flag", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetadataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = GuardianTextSecondary,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = GuardianTextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = if (label.contains("IP") || label.contains("Fingerprint")) FontFamily.Monospace else FontFamily.Default
        )
    }
}
