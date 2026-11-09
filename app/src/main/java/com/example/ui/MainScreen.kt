package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.GuardianRoute
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.GuardianDarkBg
import com.example.ui.viewmodel.GuardianViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: GuardianViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentRoute by viewModel.currentRoute.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val filteredTransactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()
    val rules by viewModel.rules.collectAsStateWithLifecycle()
    val proofs by viewModel.proofs.collectAsStateWithLifecycle()
    val urlScans by viewModel.urlScans.collectAsStateWithLifecycle()
    val chartData by viewModel.chartData.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val kpiMetrics by viewModel.kpiMetrics.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedRisk by viewModel.selectedRiskFilter.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.selectedStatusFilter.collectAsStateWithLifecycle()
    val selectedTransaction by viewModel.selectedTransaction.collectAsStateWithLifecycle()
    val selectedProof by viewModel.selectedProof.collectAsStateWithLifecycle()
    val urlInput by viewModel.urlInput.collectAsStateWithLifecycle()
    val chatInput by viewModel.chatInput.collectAsStateWithLifecycle()
    val fraudThreshold by viewModel.fraudThreshold.collectAsStateWithLifecycle()
    val isSandboxActive by viewModel.isSandboxActive.collectAsStateWithLifecycle()
    val autoBlockEnabled by viewModel.autoBlockEnabled.collectAsStateWithLifecycle()
    val notificationMessage by viewModel.notificationMessage.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(notificationMessage) {
        notificationMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearNotification()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route -> viewModel.navigateTo(route) },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("main_screen_scaffold"),
            containerColor = GuardianDarkBg,
            topBar = {
                TopHeaderBar(
                    currentRoute = currentRoute,
                    alertCount = alerts.count { !it.isResolved },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSearchClick = { viewModel.navigateTo(GuardianRoute.TRANSACTIONS) },
                    onAlertsClick = { viewModel.navigateTo(GuardianRoute.ALERTS) },
                    onSimulateClick = { viewModel.simulateAttack() }
                )
            },
            bottomBar = {
                GuardianBottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route -> viewModel.navigateTo(route) },
                    modifier = Modifier.navigationBarsPadding()
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(GuardianDarkBg)
            ) {
                when (currentRoute) {
                    GuardianRoute.OVERVIEW -> {
                        OverviewScreen(
                            kpiMetrics = kpiMetrics,
                            chartPoints = chartData,
                            recentTransactions = transactions,
                            recentAlerts = alerts,
                            onSelectTransaction = { txn -> viewModel.selectTransaction(txn) },
                            onNavigateToTransactions = { viewModel.navigateTo(GuardianRoute.TRANSACTIONS) },
                            onNavigateToAlerts = { viewModel.navigateTo(GuardianRoute.ALERTS) },
                            onSimulateAttack = { viewModel.simulateAttack() }
                        )
                    }

                    GuardianRoute.TRANSACTIONS -> {
                        TransactionsScreen(
                            transactions = filteredTransactions,
                            searchQuery = searchQuery,
                            onSearchChange = { viewModel.searchQuery.value = it },
                            selectedRisk = selectedRisk,
                            onSelectRisk = { viewModel.setRiskFilter(it) },
                            selectedStatus = selectedStatus,
                            onSelectStatus = { viewModel.setStatusFilter(it) },
                            onSelectTransaction = { txn -> viewModel.selectTransaction(txn) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    GuardianRoute.FRAUD_DETECTION -> {
                        FraudDetectionScreen(
                            rules = rules,
                            threshold = fraudThreshold,
                            onThresholdChange = { viewModel.setFraudThreshold(it) },
                            onToggleRule = { id -> viewModel.toggleRule(id) },
                            onSimulateAttack = { viewModel.simulateAttack() },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    GuardianRoute.PAYMENT_PROOF -> {
                        PaymentProofScreen(
                            proofs = proofs,
                            selectedProof = selectedProof,
                            onSelectProof = { proof -> viewModel.selectProof(proof) },
                            onAnalyzeProof = { name, amt -> viewModel.analyzeProofSample(name, amt) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    GuardianRoute.URL_SCANNER -> {
                        UrlScannerScreen(
                            scans = urlScans,
                            urlInput = urlInput,
                            onUrlInputChange = { viewModel.urlInput.value = it },
                            onScanUrl = { url -> viewModel.scanUrl(url) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    GuardianRoute.ASSISTANT -> {
                        AssistantScreen(
                            messages = chatMessages,
                            chatInput = chatInput,
                            onChatInputChange = { viewModel.chatInput.value = it },
                            onSendMessage = { text -> viewModel.sendChatMessage(text) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    GuardianRoute.ALERTS -> {
                        AlertsScreen(
                            alerts = alerts,
                            onResolveAlert = { id -> viewModel.resolveAlert(id) },
                            onDismissAlert = { id -> viewModel.dismissAlert(id) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    GuardianRoute.SETTINGS -> {
                        SettingsScreen(
                            isSandboxActive = isSandboxActive,
                            onToggleSandbox = { viewModel.toggleSandbox(it) },
                            autoBlockEnabled = autoBlockEnabled,
                            onToggleAutoBlock = { viewModel.toggleAutoBlock(it) },
                            fraudThreshold = fraudThreshold,
                            onThresholdChange = { viewModel.setFraudThreshold(it) },
                            onSimulateAttack = { viewModel.simulateAttack() },
                            onResetDemoData = { viewModel.resetDemoData() },
                            onBack = { viewModel.navigateBack() }
                        )
                    }
                }

                // Selected Transaction Forensic Inspection Modal
                selectedTransaction?.let { txn ->
                    TransactionDetailDialog(
                        transaction = txn,
                        onDismiss = { viewModel.selectTransaction(null) },
                        onApprove = { approved ->
                            viewModel.showNotification("Transaction ${approved.reference} approved")
                        },
                        onBlock = { blocked ->
                            viewModel.showNotification("Transaction ${blocked.reference} blocked and sender flagged")
                        }
                    )
                }
            }
        }
    }
}
