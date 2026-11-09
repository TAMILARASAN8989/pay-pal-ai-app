package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.theme.*

@Composable
fun AssistantScreen(
    messages: List<ChatMessage>,
    chatInput: String,
    onChatInputChange: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val suggestionChips = listOf(
        "Analyze transaction TXN-8921-X9",
        "Explain homograph phishing",
        "How does OCR detect tampering?",
        "Mitigate card testing attacks"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("assistant_screen")
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Assistant Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GuardianEmeraldContainer)
                        .border(1.dp, GuardianEmerald, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = GuardianEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "AI THREAT INTELLIGENCE",
                        color = GuardianEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Intelligent Security Analyst",
                        color = GuardianTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(GuardianEmerald.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "SANDBOX INTEL",
                    color = GuardianEmerald,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Suggestions Scroll
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestionChips.forEach { prompt ->
                SuggestionChip(
                    onClick = { onSendMessage(prompt) },
                    label = { Text(prompt, fontSize = 11.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = GuardianDarkSurface,
                        labelColor = GuardianTextSecondary
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = GuardianBorder,
                        enabled = true
                    ),
                    modifier = Modifier.testTag("chip_${prompt.take(10).replace(" ", "_")}")
                )
            }
        }

        HorizontalDivider(color = GuardianBorder, thickness = 0.5.dp)

        // Chat Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(message = msg)
            }
        }

        // Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = chatInput,
                onValueChange = onChatInputChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("assistant_input_field"),
                placeholder = { Text("Ask security analyst or query transaction...", color = GuardianTextMuted, fontSize = 13.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = GuardianDarkSurface,
                    unfocusedContainerColor = GuardianDarkSurface,
                    focusedTextColor = GuardianTextPrimary,
                    unfocusedTextColor = GuardianTextPrimary,
                    focusedBorderColor = GuardianEmerald,
                    unfocusedBorderColor = GuardianBorder
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            IconButton(
                onClick = { onSendMessage(null) },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GuardianEmerald)
                    .testTag("assistant_send_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color(0xFF022C22),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 2.dp)
        ) {
            Text(
                text = if (isUser) "Security Auditor" else "AI Guardian Intelligence",
                color = if (isUser) GuardianCyan else GuardianEmerald,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message.timestamp,
                color = GuardianTextMuted,
                fontSize = 10.sp
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) GuardianSurfaceVariant else GuardianDarkSurface
            ),
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) GuardianCyan.copy(alpha = 0.3f) else GuardianBorder
            ),
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Text(
                text = message.text,
                color = GuardianTextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
