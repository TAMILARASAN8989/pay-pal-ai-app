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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UrlScanResult
import com.example.ui.theme.*

@Composable
fun UrlScannerScreen(
    scans: List<UrlScanResult>,
    urlInput: String,
    onUrlInputChange: (String) -> Unit,
    onScanUrl: (String?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val presetUrls = listOf(
        Pair("Homograph Phish", "https://paypaI-resolution-center.support-desk.xyz/verify"),
        Pair("Official Sandbox", "https://api-m.sandbox.paypal.com/v2/checkout/orders"),
        Pair("Insecure HTTP", "http://invoice-paypal-client-confirm.tk/bill/pay")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("url_scanner_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "URL & PHISHING SCANNER",
                    color = GuardianEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Payment Gateway Link Verifier",
                    color = GuardianTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Analyzes checkout redirects, homograph glyph spoofing (e.g. Cyrillic 'I'), SSL cipher validity, and domain reputation.",
                    color = GuardianTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Search / Scan Input Box
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
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = onUrlInputChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("url_scanner_input"),
                        placeholder = { Text("https://example-payment-link.com/checkout", color = GuardianTextMuted, fontSize = 12.sp) },
                        label = { Text("Destination URL / Checkout Redirect", fontSize = 11.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = GuardianCyan)
                        },
                        trailingIcon = {
                            if (urlInput.isNotEmpty()) {
                                IconButton(onClick = { onUrlInputChange("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = GuardianTextSecondary)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GuardianTextPrimary,
                            unfocusedTextColor = GuardianTextPrimary,
                            focusedBorderColor = GuardianEmerald,
                            unfocusedBorderColor = GuardianBorder
                        ),
                        singleLine = true
                    )

                    // Presets
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Test Targets:",
                            color = GuardianTextMuted,
                            fontSize = 11.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        presetUrls.forEach { (label, url) ->
                            SuggestionChip(
                                onClick = { onUrlInputChange(url) },
                                label = { Text(label, fontSize = 11.sp) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = GuardianDarkBg,
                                    labelColor = GuardianTextSecondary
                                ),
                                border = SuggestionChipDefaults.suggestionChipBorder(
                                    borderColor = GuardianBorder,
                                    enabled = true
                                )
                            )
                        }
                    }

                    Button(
                        onClick = { onScanUrl(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = GuardianCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("execute_url_scan_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFF083344),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Execute Deep Threat Scan",
                            color = Color(0xFF083344),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Recent Scans Feed
        item {
            Text(
                text = "INSPECTED DOMAINS & REDIRECTS",
                color = GuardianTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        items(scans) { scan ->
            val isSafe = scan.reputationScore >= 70
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scan_result_${scan.id}")
                    .border(
                        1.dp,
                        if (isSafe) GuardianEmerald.copy(alpha = 0.4f) else GuardianRose.copy(alpha = 0.4f),
                        RoundedCornerShape(14.dp)
                    ),
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = scan.domain,
                                color = GuardianTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "IP: ${scan.ipAddress} • Age: ${scan.domainAgeDays} days",
                                color = GuardianTextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        // Score Badge
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${scan.reputationScore}/100",
                                color = if (isSafe) GuardianEmerald else GuardianRose,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isSafe) "Trusted" else "Malicious",
                                color = if (isSafe) GuardianEmerald else GuardianRose,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (scan.homographAttackDetected) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = GuardianRoseContainer),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = GuardianRose,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "HOMOGRAPH ATTACK DETECTED: Spoofs ${scan.typosquattingTarget ?: "PayPal Brand"}",
                                    color = Color(0xFFFFE4E6),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = GuardianBorder, thickness = 0.5.dp)

                    // Threat findings
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "SECURITY AUDIT FINDINGS",
                            color = GuardianCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        scan.threatDetails.forEach { detail ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isSafe) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (isSafe) GuardianEmerald else GuardianRose,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = detail,
                                    color = GuardianTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SSL: ${if (scan.sslValid) "Valid (${scan.sslIssuer})" else "Invalid/None"}",
                            color = if (scan.sslValid) GuardianEmerald else GuardianRose,
                            fontSize = 10.sp
                        )
                        Text(
                            text = scan.scanTimestamp,
                            color = GuardianTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
