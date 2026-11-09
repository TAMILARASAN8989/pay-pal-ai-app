package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentProofAnalysis
import com.example.ui.theme.*

@Composable
fun PaymentProofScreen(
    proofs: List<PaymentProofAnalysis>,
    selectedProof: PaymentProofAnalysis?,
    onSelectProof: (PaymentProofAnalysis) -> Unit,
    onAnalyzeProof: (String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var customDocName by remember { mutableStateOf("") }
    var customAmount by remember { mutableStateOf("") }

    val activeProof = selectedProof ?: proofs.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("payment_proof_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "OCR & FORENSIC PROOF ANALYSIS",
                    color = GuardianEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Receipt & Wire Tamper Verification",
                    color = GuardianTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Inspects payment screenshots for font rasterization anomalies, digital signature hashes, and pixel quantization tampering.",
                    color = GuardianTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Active Analysis Report Card (Hero)
        if (activeProof != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_proof_report")
                        .border(
                            1.dp,
                            when (activeProof.verdict) {
                                "AUTHENTIC" -> GuardianEmerald
                                "MANIPULATED" -> GuardianRose
                                else -> GuardianAmber
                            },
                            RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = activeProof.sampleName,
                                    color = GuardianTextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = activeProof.documentType,
                                    color = GuardianTextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            // Verdict Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when (activeProof.verdict) {
                                            "AUTHENTIC" -> GuardianEmerald.copy(alpha = 0.15f)
                                            "MANIPULATED" -> GuardianRose.copy(alpha = 0.15f)
                                            else -> GuardianAmber.copy(alpha = 0.15f)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when (activeProof.verdict) {
                                            "AUTHENTIC" -> GuardianEmerald
                                            "MANIPULATED" -> GuardianRose
                                            else -> GuardianAmber
                                        },
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = activeProof.verdict,
                                    color = when (activeProof.verdict) {
                                        "AUTHENTIC" -> GuardianEmerald
                                        "MANIPULATED" -> GuardianRose
                                        else -> GuardianAmber
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        HorizontalDivider(color = GuardianBorder, thickness = 0.5.dp)

                        // Forensic Scores Matrix
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ForensicMetricBox(
                                label = "Kerning Alignment",
                                value = "${activeProof.fontKerningScore}%",
                                isPositive = activeProof.fontKerningScore > 80,
                                modifier = Modifier.weight(1f)
                            )
                            ForensicMetricBox(
                                label = "Tamper Noise Score",
                                value = "${activeProof.tamperScore}/100",
                                isPositive = activeProof.tamperScore < 30,
                                modifier = Modifier.weight(1f)
                            )
                            ForensicMetricBox(
                                label = "Digital Seal",
                                value = if (activeProof.digitalSignatureValid) "MATCH" else "INVALID",
                                isPositive = activeProof.digitalSignatureValid,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Anomalies Breakdown
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "FORENSIC SCAN FINDINGS",
                                color = GuardianCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            activeProof.detectedAnomalies.forEach { anomaly ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (activeProof.verdict == "AUTHENTIC") Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (activeProof.verdict == "AUTHENTIC") GuardianEmerald else GuardianRose,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = anomaly,
                                        color = GuardianTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Test / Upload Simulator Form
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
                        text = "SIMULATE PROOF FORENSIC INSPECTION",
                        color = GuardianTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = customDocName,
                        onValueChange = { customDocName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("proof_name_input"),
                        placeholder = { Text("e.g. Wire Transfer Receipt #9901", color = GuardianTextMuted, fontSize = 12.sp) },
                        label = { Text("Document / Image Label", fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GuardianTextPrimary,
                            unfocusedTextColor = GuardianTextPrimary,
                            focusedBorderColor = GuardianEmerald,
                            unfocusedBorderColor = GuardianBorder
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = customAmount,
                        onValueChange = { customAmount = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("proof_amount_input"),
                        placeholder = { Text("e.g. $450.00 USD", color = GuardianTextMuted, fontSize = 12.sp) },
                        label = { Text("Reported Payment Amount", fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GuardianTextPrimary,
                            unfocusedTextColor = GuardianTextPrimary,
                            focusedBorderColor = GuardianEmerald,
                            unfocusedBorderColor = GuardianBorder
                        ),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            val name = customDocName.ifBlank { "Custom Receipt Sample" }
                            val amt = customAmount.ifBlank { "$1,250.00 USD" }
                            onAnalyzeProof(name, amt)
                            customDocName = ""
                            customAmount = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GuardianEmerald),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("analyze_proof_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = Color(0xFF022C22),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Execute Forensic Analysis",
                            color = Color(0xFF022C22),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Available Sample Proofs
        item {
            Text(
                text = "AVAILABLE TEST SAMPLES",
                color = GuardianTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        items(proofs) { proof ->
            val isSelected = activeProof?.id == proof.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("proof_item_${proof.id}")
                    .border(
                        1.dp,
                        if (isSelected) GuardianEmerald else GuardianBorder,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelectProof(proof) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) GuardianDarkSurface else GuardianDarkBg
                ),
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
                        Text(
                            text = proof.sampleName,
                            color = GuardianTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${proof.documentType} • Amount: ${proof.reportedAmount}",
                            color = GuardianTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = proof.verdict,
                        color = when (proof.verdict) {
                            "AUTHENTIC" -> GuardianEmerald
                            "MANIPULATED" -> GuardianRose
                            else -> GuardianAmber
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ForensicMetricBox(
    label: String,
    value: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = GuardianDarkBg),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPositive) GuardianEmerald.copy(alpha = 0.4f) else GuardianRose.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = GuardianTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = if (isPositive) GuardianEmerald else GuardianRose,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
