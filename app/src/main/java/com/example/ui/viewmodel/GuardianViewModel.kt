package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.GuardianRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GuardianViewModel(
    private val repository: GuardianRepository = GuardianRepository()
) : ViewModel() {

    // Navigation state
    private val _currentRoute = MutableStateFlow(GuardianRoute.OVERVIEW)
    val currentRoute: StateFlow<GuardianRoute> = _currentRoute.asStateFlow()

    private val navStack = mutableListOf<GuardianRoute>(GuardianRoute.OVERVIEW)

    // Global Search & Filtering
    val searchQuery = MutableStateFlow("")
    val selectedRiskFilter = MutableStateFlow<RiskLevel?>(null)
    val selectedStatusFilter = MutableStateFlow<TransactionStatus?>(null)

    // Selected items for modal/detail sheet inspection
    val selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedProof = MutableStateFlow<PaymentProofAnalysis?>(null)

    // Input fields
    val urlInput = MutableStateFlow("")
    val chatInput = MutableStateFlow("")

    // Notification / Toast
    val notificationMessage = MutableStateFlow<String?>(null)

    // Data streams from repository
    val transactions: StateFlow<List<Transaction>> = repository.transactions
    val alerts: StateFlow<List<SecurityAlert>> = repository.alerts
    val rules: StateFlow<List<FraudRule>> = repository.rules
    val proofs: StateFlow<List<PaymentProofAnalysis>> = repository.proofs
    val urlScans: StateFlow<List<UrlScanResult>> = repository.urlScans
    val chartData: StateFlow<List<ActivityChartPoint>> = repository.chartData
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    val fraudThreshold: StateFlow<Float> = repository.fraudThreshold
    val isSandboxActive: StateFlow<Boolean> = repository.isSandboxActive
    val autoBlockEnabled: StateFlow<Boolean> = repository.autoBlockEnabled

    // Filtered transactions based on search query, risk level, and status
    val filteredTransactions: StateFlow<List<Transaction>> = combine(
        transactions,
        searchQuery,
        selectedRiskFilter,
        selectedStatusFilter
    ) { txns, query, risk, status ->
        txns.filter { txn ->
            val matchesQuery = query.isBlank() ||
                    txn.reference.contains(query, ignoreCase = true) ||
                    txn.senderName.contains(query, ignoreCase = true) ||
                    txn.recipientName.contains(query, ignoreCase = true) ||
                    txn.amount.toString().contains(query) ||
                    txn.id.contains(query, ignoreCase = true)

            val matchesRisk = risk == null || txn.riskLevel == risk
            val matchesStatus = status == null || txn.status == status

            matchesQuery && matchesRisk && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // KPI Metrics
    val kpiMetrics: StateFlow<KpiMetrics> = transactions.map {
        repository.getKpiMetrics()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        repository.getKpiMetrics()
    )

    fun navigateTo(route: GuardianRoute) {
        if (_currentRoute.value != route) {
            navStack.add(route)
            _currentRoute.value = route
        }
    }

    fun navigateBack(): Boolean {
        return if (navStack.size > 1) {
            navStack.removeAt(navStack.size - 1)
            _currentRoute.value = navStack.last()
            true
        } else {
            false
        }
    }

    fun selectTransaction(transaction: Transaction?) {
        selectedTransaction.value = transaction
    }

    fun selectProof(proof: PaymentProofAnalysis?) {
        selectedProof.value = proof
    }

    fun setRiskFilter(risk: RiskLevel?) {
        selectedRiskFilter.value = if (selectedRiskFilter.value == risk) null else risk
    }

    fun setStatusFilter(status: TransactionStatus?) {
        selectedStatusFilter.value = if (selectedStatusFilter.value == status) null else status
    }

    fun toggleRule(ruleId: String) {
        repository.toggleRule(ruleId)
        showNotification("Security rule configuration updated")
    }

    fun resolveAlert(alertId: String) {
        repository.resolveAlert(alertId)
        showNotification("Alert marked as resolved / mitigated")
    }

    fun dismissAlert(alertId: String) {
        repository.dismissAlert(alertId)
        showNotification("Alert dismissed from feed")
    }

    fun scanUrl(targetUrl: String? = null) {
        val url = targetUrl ?: urlInput.value
        if (url.isNotBlank()) {
            val result = repository.scanUrl(url)
            urlInput.value = ""
            showNotification("URL Scan completed: ${result.classification}")
        }
    }

    fun analyzeProofSample(name: String, amount: String) {
        val proof = repository.analyzePaymentProof(name, amount)
        selectedProof.value = proof
        showNotification("Proof analyzed: ${proof.verdict}")
    }

    fun sendChatMessage(text: String? = null) {
        val message = text ?: chatInput.value
        if (message.isNotBlank()) {
            repository.sendChatMessage(message)
            chatInput.value = ""
        }
    }

    fun simulateAttack() {
        repository.simulateAttackTrigger()
        showNotification("Adversarial attack simulated and intercepted")
    }

    fun setFraudThreshold(value: Float) {
        repository.setFraudThreshold(value)
    }

    fun toggleSandbox(active: Boolean) {
        repository.toggleSandbox(active)
        showNotification(if (active) "Sandbox mode active" else "Production simulation mode")
    }

    fun toggleAutoBlock(enabled: Boolean) {
        repository.toggleAutoBlock(enabled)
        showNotification(if (enabled) "Automated high-risk blocking active" else "Manual review mode active")
    }

    fun resetDemoData() {
        repository.resetDemoData()
        searchQuery.value = ""
        selectedRiskFilter.value = null
        selectedStatusFilter.value = null
        showNotification("Demo environment data reset to pristine state")
    }

    fun showNotification(message: String) {
        notificationMessage.value = message
    }

    fun clearNotification() {
        notificationMessage.value = null
    }
}
