package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.model.GuardianRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeaderBar(
    currentRoute: GuardianRoute,
    alertCount: Int,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onSimulateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_header_bar"),
        color = GuardianDarkSurface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, GuardianBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .testTag("nav_menu_button")
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu",
                            tint = GuardianTextPrimary
                        )
                    }

                    // Logo Icon with subtle glowing ring
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(GuardianEmeraldContainer)
                            .border(1.dp, GuardianEmerald, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = GuardianEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "PAYPAL AI GUARDIAN",
                                color = GuardianTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Intelligent Payment Protection",
                            color = GuardianTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Quick Attack Simulation Button
                    IconButton(
                        onClick = onSimulateClick,
                        modifier = Modifier
                            .testTag("header_simulate_attack_button")
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Simulate Adversarial Attack",
                            tint = GuardianAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Search Trigger Button
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .testTag("header_search_button")
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Transactions",
                            tint = GuardianTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Alerts Bell with Badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onAlertsClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Security Alerts",
                            tint = if (alertCount > 0) GuardianRose else GuardianTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        if (alertCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 2.dp, end = 2.dp)
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .background(GuardianRose),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$alertCount",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Demo environment pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(GuardianEmerald)
                    )
                    Text(
                        text = "SANDBOX ENVIRONMENT",
                        color = GuardianEmerald,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "• Independent Prototype",
                        color = GuardianTextMuted,
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = currentRoute.routeKey,
                    color = GuardianTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun NavigationDrawerContent(
    currentRoute: GuardianRoute,
    onNavigate: (GuardianRoute) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
            .width(300.dp)
            .testTag("nav_drawer_sheet"),
        drawerContainerColor = GuardianDarkBg,
        drawerContentColor = GuardianTextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Drawer Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
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
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = GuardianEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "PAYPAL AI GUARDIAN",
                            color = GuardianTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Intelligent Payment Protection",
                            color = GuardianTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                HorizontalDivider(color = GuardianBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(4.dp))

                // Navigation Items
                GuardianRoute.values().forEach { route ->
                    val isSelected = currentRoute == route
                    val (icon, activeColor) = getRouteIconAndColor(route)

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = route.title,
                                tint = if (isSelected) GuardianEmerald else GuardianTextSecondary
                            )
                        },
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = route.title,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) GuardianTextPrimary else GuardianTextSecondary
                                )
                                Text(
                                    text = route.routeKey,
                                    fontSize = 10.sp,
                                    color = if (isSelected) GuardianEmerald.copy(alpha = 0.8f) else GuardianTextMuted
                                )
                            }
                        },
                        selected = isSelected,
                        onClick = {
                            onNavigate(route)
                            onCloseDrawer()
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = GuardianDarkSurface,
                            unselectedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .testTag("nav_item_${route.name.lowercase()}")
                    )
                }
            }

            // Drawer Footer
            Card(
                colors = CardDefaults.cardColors(containerColor = GuardianDarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GuardianBorder),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(GuardianEmerald)
                        )
                        Text(
                            text = "Engine: Active • TLS 1.3",
                            color = GuardianEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "Demo prototype sandbox connected. No real funds involved.",
                        color = GuardianTextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GuardianBottomNavigationBar(
    currentRoute: GuardianRoute,
    onNavigate: (GuardianRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainRoutes = listOf(
        GuardianRoute.OVERVIEW,
        GuardianRoute.TRANSACTIONS,
        GuardianRoute.FRAUD_DETECTION,
        GuardianRoute.URL_SCANNER,
        GuardianRoute.ASSISTANT
    )

    NavigationBar(
        modifier = modifier
            .testTag("bottom_nav_bar")
            .border(1.dp, GuardianBorder),
        containerColor = GuardianDarkSurface,
        contentColor = GuardianTextPrimary
    ) {
        mainRoutes.forEach { route ->
            val isSelected = currentRoute == route
            val (icon, _) = getRouteIconAndColor(route)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = route.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = route.title.take(10),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GuardianEmerald,
                    selectedTextColor = GuardianEmerald,
                    indicatorColor = GuardianEmeraldContainer.copy(alpha = 0.6f),
                    unselectedIconColor = GuardianTextSecondary,
                    unselectedTextColor = GuardianTextSecondary
                ),
                modifier = Modifier.testTag("bottom_tab_${route.name.lowercase()}")
            )
        }
    }
}

private fun getRouteIconAndColor(route: GuardianRoute): Pair<ImageVector, Color> {
    return when (route) {
        GuardianRoute.OVERVIEW -> Pair(Icons.Default.Dashboard, GuardianCyan)
        GuardianRoute.TRANSACTIONS -> Pair(Icons.Default.ReceiptLong, GuardianBlue)
        GuardianRoute.FRAUD_DETECTION -> Pair(Icons.Default.Security, GuardianRose)
        GuardianRoute.PAYMENT_PROOF -> Pair(Icons.Default.DocumentScanner, GuardianAmber)
        GuardianRoute.URL_SCANNER -> Pair(Icons.Default.Link, GuardianCyan)
        GuardianRoute.ASSISTANT -> Pair(Icons.Default.SmartToy, GuardianEmerald)
        GuardianRoute.ALERTS -> Pair(Icons.Default.Warning, GuardianRose)
        GuardianRoute.SETTINGS -> Pair(Icons.Default.Tune, GuardianTextSecondary)
    }
}
