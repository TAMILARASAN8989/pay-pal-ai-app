package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.data.model.Transaction
import com.example.data.model.TransactionStatus
import com.example.ui.components.RiskBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun TransactionsScreen(
    transactions: List<Transaction>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedRisk: RiskLevel?,
    onSelectRisk: (RiskLevel?) -> Unit,
    selectedStatus: TransactionStatus?,
    onSelectStatus: (TransactionStatus?) -> Unit,
    onSelectTransaction: (Transaction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("transactions_screen")
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "TRANSACTION REGISTRY",
                    color = GuardianEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "Audit & Forensic Logs",
                    color = GuardianTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${transactions.size} records",
                color = GuardianTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("transactions_search_input"),
            placeholder = { Text("Search by ref, sender, amount, or recipient...", color = GuardianTextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = GuardianTextSecondary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = GuardianTextSecondary
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GuardianDarkSurface,
                unfocusedContainerColor = GuardianDarkSurface,
                focusedBorderColor = GuardianEmerald,
                unfocusedBorderColor = GuardianBorder,
                focusedTextColor = GuardianTextPrimary,
                unfocusedTextColor = GuardianTextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // Filter Chips Row: Risk
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedRisk == null,
                onClick = { onSelectRisk(null) },
                label = { Text("All Risks", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GuardianDarkSurface,
                    selectedLabelColor = GuardianEmerald,
                    containerColor = GuardianDarkBg,
                    labelColor = GuardianTextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (selectedRisk == null) GuardianEmerald else GuardianBorder,
                    enabled = true,
                    selected = selectedRisk == null
                )
            )

            RiskLevel.values().forEach { risk ->
                val isSelected = selectedRisk == risk
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectRisk(risk) },
                    label = { Text(risk.label, fontSize = 11.sp) },
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

        // Transactions List or Empty State
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("transactions_empty_state"),
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
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAltOff,
                            contentDescription = null,
                            tint = GuardianTextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No matching transactions",
                            color = GuardianTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Adjust your search keywords or clear risk/status filters to view available sandbox records.",
                            color = GuardianTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                        Button(
                            onClick = {
                                onSearchChange("")
                                onSelectRisk(null)
                                onSelectStatus(null)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GuardianEmeraldContainer),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Reset Filters", color = GuardianEmerald, fontSize = 12.sp)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(transactions) { txn ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("txn_card_${txn.reference}")
                            .border(1.dp, GuardianBorder, RoundedCornerShape(12.dp))
                            .clickable { onSelectTransaction(txn) },
                        colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                    text = "$${String.format("%.2f", txn.amount)} ${txn.currency}",
                                    color = GuardianTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "From: ${txn.senderName}",
                                        color = GuardianTextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "To: ${txn.recipientName}",
                                        color = GuardianTextMuted,
                                        fontSize = 11.sp
                                    )
                                }

                                RiskBadge(riskLevel = txn.riskLevel)
                            }

                            HorizontalDivider(color = GuardianBorder.copy(alpha = 0.5f), thickness = 0.5.dp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "IP: ${txn.ipAddress.take(24)}",
                                    color = GuardianTextMuted,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = txn.timestamp,
                                    color = GuardianTextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
