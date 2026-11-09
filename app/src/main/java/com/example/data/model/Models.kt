package com.example.data.model

enum class RiskLevel(val label: String, val badgeColor: Long) {
    LOW("Low Risk", 0xFF10B981),
    MEDIUM("Medium Risk", 0xFFF59E0B),
    HIGH("High Risk", 0xFFF43F5E),
    CRITICAL("Critical Risk", 0xFFE11D48)
}

enum class TransactionStatus(val label: String) {
    VERIFIED("Verified Sandbox"),
    PENDING("Pending Review"),
    FLAGGED("Flagged Suspicious"),
    BLOCKED("Payment Blocked")
}

data class Transaction(
    val id: String,
    val reference: String,
    val amount: Double,
    val currency: String = "USD",
    val senderName: String,
    val senderEmail: String,
    val recipientName: String,
    val recipientEmail: String,
    val timestamp: String,
    val status: TransactionStatus,
    val riskLevel: RiskLevel,
    val riskScore: Int,
    val paymentMethod: String,
    val ipAddress: String,
    val location: String,
    val deviceFingerprint: String,
    val fraudSignals: List<String>,
    val isSandbox: Boolean = true
)

data class SecurityAlert(
    val id: String,
    val title: String,
    val description: String,
    val severity: RiskLevel,
    val timestamp: String,
    val affectedRef: String,
    val isResolved: Boolean = false,
    val category: String
)

data class FraudRule(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val riskWeight: Int,
    val isEnabled: Boolean,
    val triggerCount: Int,
    val lastTriggered: String
)

data class PaymentProofAnalysis(
    val id: String,
    val sampleName: String,
    val documentType: String,
    val reportedAmount: String,
    val reportedTimestamp: String,
    val fontKerningScore: Int,
    val recipientMatch: Boolean,
    val tamperScore: Int, // 0 = pristine, 100 = heavy tampering
    val verdict: String, // "AUTHENTIC", "MANIPULATED", "SUSPICIOUS"
    val detectedAnomalies: List<String>,
    val digitalSignatureValid: Boolean
)

data class UrlScanResult(
    val id: String,
    val targetUrl: String,
    val domain: String,
    val ipAddress: String,
    val sslValid: Boolean,
    val sslIssuer: String,
    val domainAgeDays: Int,
    val homographAttackDetected: Boolean,
    val typosquattingTarget: String?,
    val reputationScore: Int, // 0 to 100 (100 = safest)
    val classification: String,
    val threatDetails: List<String>,
    val scanTimestamp: String
)

data class KpiMetrics(
    val totalTransactions: Int,
    val verifiedPayments: Int,
    val suspiciousCount: Int,
    val reviewRequiredCount: Int,
    val totalVolumeUsd: Double,
    val preventedFraudUsd: Double
)

data class ActivityChartPoint(
    val day: String,
    val volume: Float,
    val flagCount: Float
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String,
    val referenceId: String? = null
)

enum class GuardianRoute(val title: String, val routeKey: String) {
    OVERVIEW("Overview", "/dashboard"),
    TRANSACTIONS("Transactions", "/transactions"),
    FRAUD_DETECTION("Fraud Detection", "/fraud-detection"),
    PAYMENT_PROOF("Proof Analysis", "/payment-proof"),
    URL_SCANNER("URL Scanner", "/url-scanner"),
    ASSISTANT("AI Assistant", "/assistant"),
    ALERTS("Security Alerts", "/alerts"),
    SETTINGS("Settings", "/settings")
}
