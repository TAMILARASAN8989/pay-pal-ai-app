package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class GuardianRepository {

    private val initialTransactions = listOf(
        Transaction(
            id = "txn-001",
            reference = "TXN-8921-X9",
            amount = 1240.50,
            currency = "USD",
            senderName = "Marcus Sterling",
            senderEmail = "marcus.sterling@devcorp.internal",
            recipientName = "Acme Cloud Services",
            recipientEmail = "billing@acmecloud.com",
            timestamp = "Today, 14:32:10 UTC",
            status = TransactionStatus.FLAGGED,
            riskLevel = RiskLevel.HIGH,
            riskScore = 88,
            paymentMethod = "Sandbox Linked Bank (ACH)",
            ipAddress = "185.220.101.44 (Tor Exit Relay)",
            location = "Frankfurt, DE",
            deviceFingerprint = "dev_fp_9x92b_unrecognized",
            fraudSignals = listOf(
                "Tor Exit Node / Anonymizer IP detected",
                "Geo-velocity anomaly: 6,400km shift in 22 minutes",
                "Transaction velocity exceeded 5x user baseline"
            ),
            isSandbox = true
        ),
        Transaction(
            id = "txn-002",
            reference = "TXN-7140-B4",
            amount = 45.00,
            currency = "USD",
            senderName = "Elena Rostova",
            senderEmail = "elena.r@fintech-demo.io",
            recipientName = "SaaS Monthly Subscription",
            recipientEmail = "pay@saasflow.io",
            timestamp = "Today, 14:15:02 UTC",
            status = TransactionStatus.VERIFIED,
            riskLevel = RiskLevel.LOW,
            riskScore = 8,
            paymentMethod = "Sandbox Visa •••• 4242",
            ipAddress = "24.180.88.19",
            location = "New York, US",
            deviceFingerprint = "dev_fp_chrome_macOS_verified",
            fraudSignals = emptyList(),
            isSandbox = true
        ),
        Transaction(
            id = "txn-003",
            reference = "TXN-4912-M7",
            amount = 3890.00,
            currency = "USD",
            senderName = "Alpha Venture Holdings",
            senderEmail = "treasury@alphaventure-corp.com",
            recipientName = "Overseas Contractor LLC",
            recipientEmail = "payouts@overseas-devs.cc",
            timestamp = "Today, 13:48:44 UTC",
            status = TransactionStatus.PENDING,
            riskLevel = RiskLevel.MEDIUM,
            riskScore = 54,
            paymentMethod = "Sandbox PayPal Balance",
            ipAddress = "104.28.19.124",
            location = "London, GB",
            deviceFingerprint = "dev_fp_firefox_win11_new",
            fraudSignals = listOf(
                "Unusual first-time recipient with .cc top-level domain",
                "High transaction amount requires secondary authorization"
            ),
            isSandbox = true
        ),
        Transaction(
            id = "txn-004",
            reference = "TXN-3291-K2",
            amount = 899.99,
            currency = "USD",
            senderName = "David Kim",
            senderEmail = "dkim991@sandbox-user.net",
            recipientName = "Global Digital Gift Cards",
            recipientEmail = "orders@fastgiftcards-api.com",
            timestamp = "Today, 12:20:19 UTC",
            status = TransactionStatus.BLOCKED,
            riskLevel = RiskLevel.CRITICAL,
            riskScore = 96,
            paymentMethod = "Sandbox Mastercard •••• 5555",
            ipAddress = "194.26.29.112 (Bulletproof Hosting ASN)",
            location = "Amsterdam, NL",
            deviceFingerprint = "dev_fp_spoofed_useragent",
            fraudSignals = listOf(
                "Automated card testing pattern recognized (BIN 555544)",
                "Blacklisted high-risk merchant category (Digital Vouchers)",
                "Browser canvas fingerprint manipulation detected"
            ),
            isSandbox = true
        ),
        Transaction(
            id = "txn-005",
            reference = "TXN-1052-P1",
            amount = 120.00,
            currency = "USD",
            senderName = "Sophia Chen",
            senderEmail = "sophia.c@designstudio.co",
            recipientName = "Figma Design Tools",
            recipientEmail = "billing@figma.com",
            timestamp = "Today, 11:05:31 UTC",
            status = TransactionStatus.VERIFIED,
            riskLevel = RiskLevel.LOW,
            riskScore = 12,
            paymentMethod = "Sandbox PayPal Wallet",
            ipAddress = "73.222.14.88",
            location = "San Francisco, US",
            deviceFingerprint = "dev_fp_safari_ios_trusted",
            fraudSignals = emptyList(),
            isSandbox = true
        ),
        Transaction(
            id = "txn-006",
            reference = "TXN-6628-Q3",
            amount = 2450.00,
            currency = "USD",
            senderName = "TechNova Labs",
            senderEmail = "accounts@technovalabs.ai",
            recipientName = "GPU Cloud Computing Node",
            recipientEmail = "billing@gpuhosting-node.io",
            timestamp = "Today, 09:41:18 UTC",
            status = TransactionStatus.PENDING,
            riskLevel = RiskLevel.MEDIUM,
            riskScore = 48,
            paymentMethod = "Sandbox Corporate Amex •••• 0005",
            ipAddress = "172.56.42.190",
            location = "Austin, US",
            deviceFingerprint = "dev_fp_edge_win10_known",
            fraudSignals = listOf(
                "Corporate card limit threshold check triggered",
                "New vendor authorization required"
            ),
            isSandbox = true
        ),
        Transaction(
            id = "txn-007",
            reference = "TXN-9904-Z8",
            amount = 512.40,
            currency = "USD",
            senderName = "Liam Gallagher",
            senderEmail = "liam.g@soundwav.org",
            recipientName = "Audio Hardware Depot",
            recipientEmail = "checkout@audiodepot.net",
            timestamp = "Yesterday, 22:18:04 UTC",
            status = TransactionStatus.VERIFIED,
            riskLevel = RiskLevel.LOW,
            riskScore = 15,
            paymentMethod = "Sandbox PayPal Balance",
            ipAddress = "82.165.197.1",
            location = "Manchester, GB",
            deviceFingerprint = "dev_fp_chrome_linux_trusted",
            fraudSignals = emptyList(),
            isSandbox = true
        ),
        Transaction(
            id = "txn-008",
            reference = "TXN-5519-R6",
            amount = 5400.00,
            currency = "USD",
            senderName = "Katarina Volkova",
            senderEmail = "k.volkova@globalremit.biz",
            recipientName = "Cryptocurrency Liquidity Pool",
            recipientEmail = "deposit@p2p-escrow-desk.su",
            timestamp = "Yesterday, 19:10:55 UTC",
            status = TransactionStatus.BLOCKED,
            riskLevel = RiskLevel.CRITICAL,
            riskScore = 98,
            paymentMethod = "Sandbox Wire Equivalent",
            ipAddress = "91.240.118.82",
            location = "Almaty, KZ",
            deviceFingerprint = "dev_fp_headless_puppeteer",
            fraudSignals = listOf(
                "Headless Chromium browser script execution detected",
                "Unregistered high-risk .su domain escrow target",
                "Known money-mule remittance pattern match"
            ),
            isSandbox = true
        )
    )

    private val initialAlerts = listOf(
        SecurityAlert(
            id = "alt-001",
            title = "High Geo-Velocity Anomaly",
            description = "Account login in Frankfurt, Germany immediately followed by transaction initiation from Tor relay.",
            severity = RiskLevel.HIGH,
            timestamp = "14:32:10 UTC",
            affectedRef = "TXN-8921-X9",
            category = "Velocity Anomaly"
        ),
        SecurityAlert(
            id = "alt-002",
            title = "Credential Stuffing & Card Testing",
            description = "Rapid card authorization attempts detected across 14 merchant sandboxes within 90 seconds.",
            severity = RiskLevel.CRITICAL,
            timestamp = "12:20:19 UTC",
            affectedRef = "TXN-3291-K2",
            category = "Bot Attack"
        ),
        SecurityAlert(
            id = "alt-003",
            title = "Homograph Domain In Invoice Link",
            description = "Detected Cyrillic 'a' character substitution in sandbox notification URL: paypаl-verify.net",
            severity = RiskLevel.HIGH,
            timestamp = "11:50:00 UTC",
            affectedRef = "SEC-URL-401",
            category = "Phishing / Homograph"
        ),
        SecurityAlert(
            id = "alt-004",
            title = "OCR Font Tamper Discrepancy",
            description = "Forensic inspection detected font rasterization misalignment on uploaded receipt amount.",
            severity = RiskLevel.MEDIUM,
            timestamp = "10:14:22 UTC",
            affectedRef = "PRF-SAMPLE-02",
            category = "Proof Forgery"
        ),
        SecurityAlert(
            id = "alt-005",
            title = "Unusual Night-Time Volume Spike",
            description = "Dormant sandbox business account surged from $0/week to $12,400 within 2 hours.",
            severity = RiskLevel.MEDIUM,
            timestamp = "08:02:11 UTC",
            affectedRef = "ACC-78192-SANDBOX",
            category = "Behavioral Spike"
        )
    )

    private val initialRules = listOf(
        FraudRule(
            id = "rule-1",
            name = "Tor & Anonymizing Proxy Blocker",
            description = "Automatically flags or blocks requests arriving from known Tor exit nodes, commercial VPN proxies, or datacenter IP pools.",
            category = "Network Threat",
            riskWeight = 40,
            isEnabled = true,
            triggerCount = 142,
            lastTriggered = "14:32 UTC"
        ),
        FraudRule(
            id = "rule-2",
            name = "Geo-Velocity Impossible Travel",
            description = "Calculates physical travel speed between consecutive logins and transactions; triggers if speed exceeds 900 km/h.",
            category = "Behavioral",
            riskWeight = 35,
            isEnabled = true,
            triggerCount = 89,
            lastTriggered = "14:32 UTC"
        ),
        FraudRule(
            id = "rule-3",
            name = "Headless Browser & Bot Detection",
            description = "Analyzes navigator.webdriver flags, WebGL renderer fingerprints, and mouse telemetry to intercept automated scripts.",
            category = "Device Integrity",
            riskWeight = 45,
            isEnabled = true,
            triggerCount = 310,
            lastTriggered = "12:20 UTC"
        ),
        FraudRule(
            id = "rule-4",
            name = "First-Time High-Value Threshold",
            description = "Routes transactions over $2,500 to manual compliance review when the recipient domain has less than 30 days history.",
            category = "Transaction",
            riskWeight = 25,
            isEnabled = true,
            triggerCount = 47,
            lastTriggered = "13:48 UTC"
        ),
        FraudRule(
            id = "rule-5",
            name = "Card Testing Velocity Guard",
            description = "Flags payment attempts if more than 3 distinct card numbers are used on the same device fingerprint within 5 minutes.",
            category = "Card Security",
            riskWeight = 50,
            isEnabled = true,
            triggerCount = 219,
            lastTriggered = "Yesterday"
        )
    )

    private val sampleProofs = listOf(
        PaymentProofAnalysis(
            id = "PRF-001",
            sampleName = "Official Sandbox Receipt #9810",
            documentType = "PayPal Sandbox Transaction Receipt",
            reportedAmount = "$45.00 USD",
            reportedTimestamp = "Nov 08, 2026 - 14:15 UTC",
            fontKerningScore = 98,
            recipientMatch = true,
            tamperScore = 4,
            verdict = "AUTHENTIC",
            detectedAnomalies = listOf(
                "Digital signature hash matched sandbox ledger",
                "Font kerning and pixel alignment pristine",
                "Transaction ID cross-verified with mock database"
            ),
            digitalSignatureValid = true
        ),
        PaymentProofAnalysis(
            id = "PRF-002",
            sampleName = "Forged Wire Confirmation Screenshot",
            documentType = "Manipulated Mobile Payment Screenshot",
            reportedAmount = "$12,500.00 USD",
            reportedTimestamp = "Nov 08, 2026 - 02:40 UTC",
            fontKerningScore = 38,
            recipientMatch = false,
            tamperScore = 89,
            verdict = "MANIPULATED",
            detectedAnomalies = listOf(
                "Inconsistent font rasterization around amount text ($12,500)",
                "JPEG quantization artifact boundary detected at total line",
                "Timestamp typography differs from authentic PayPal mobile theme",
                "Recipient email domain does not match verified merchant ID"
            ),
            digitalSignatureValid = false
        ),
        PaymentProofAnalysis(
            id = "PRF-003",
            sampleName = "Suspicious Altered PDF Invoice",
            documentType = "Supplier Commercial Invoice",
            reportedAmount = "$3,890.00 USD",
            reportedTimestamp = "Nov 07, 2026 - 19:20 UTC",
            fontKerningScore = 64,
            recipientMatch = true,
            tamperScore = 58,
            verdict = "SUSPICIOUS",
            detectedAnomalies = listOf(
                "PDF metadata creation date is newer than transaction date",
                "Secondary layer detected modifying banking routing coordinates",
                "Review recommended before manual payout release"
            ),
            digitalSignatureValid = false
        )
    )

    private val sampleUrls = listOf(
        UrlScanResult(
            id = "url-001",
            targetUrl = "https://paypaI-resolution-center.support-desk.xyz/verify?id=992",
            domain = "paypaI-resolution-center.support-desk.xyz",
            ipAddress = "185.196.220.89",
            sslValid = true,
            sslIssuer = "Let's Encrypt (Free Tier)",
            domainAgeDays = 3,
            homographAttackDetected = true,
            typosquattingTarget = "PayPal.com (Cyrillic 'I' substituted for 'l')",
            reputationScore = 4,
            classification = "Malicious Phishing Portal",
            threatDetails = listOf(
                "Homograph glyph substitution detected in domain name",
                "Domain registered only 3 days ago via privacy proxy",
                "Hosting ASN associated with known credential harvesting campaigns",
                "Fake PayPal login credential capture form detected"
            ),
            scanTimestamp = "Just now"
        ),
        UrlScanResult(
            id = "url-002",
            targetUrl = "https://api-m.sandbox.paypal.com/v2/checkout/orders",
            domain = "sandbox.paypal.com",
            ipAddress = "151.101.65.140",
            sslValid = true,
            sslIssuer = "DigiCert Global Root G2 (PayPal Official EV)",
            domainAgeDays = 8940,
            homographAttackDetected = false,
            typosquattingTarget = null,
            reputationScore = 100,
            classification = "Legitimate Sandbox Endpoint",
            threatDetails = listOf(
                "Official verified PayPal sandbox developer domain",
                "Valid high-assurance SSL certificate",
                "Secure TLS 1.3 cryptographic cipher suite"
            ),
            scanTimestamp = "2 mins ago"
        ),
        UrlScanResult(
            id = "url-003",
            targetUrl = "http://invoice-paypal-client-confirm.tk/bill/pay",
            domain = "invoice-paypal-client-confirm.tk",
            ipAddress = "45.142.122.10",
            sslValid = false,
            sslIssuer = "Self-Signed / Untrusted",
            domainAgeDays = 1,
            homographAttackDetected = false,
            typosquattingTarget = "PayPal Brand Trademark Infringement",
            reputationScore = 2,
            classification = "Unsecured Phishing / Insecure HTTP",
            threatDetails = listOf(
                "No valid SSL certificate; transmits credentials in cleartext HTTP",
                "Suspicious free top-level domain (.tk)",
                "Domain blacklisted on 8 cyber threat intelligence feeds"
            ),
            scanTimestamp = "15 mins ago"
        )
    )

    private val chartPoints = listOf(
        ActivityChartPoint("Mon", 24f, 2f),
        ActivityChartPoint("Tue", 38f, 5f),
        ActivityChartPoint("Wed", 45f, 3f),
        ActivityChartPoint("Thu", 52f, 8f),
        ActivityChartPoint("Fri", 68f, 4f),
        ActivityChartPoint("Sat", 41f, 1f),
        ActivityChartPoint("Sun", 59f, 6f)
    )

    // State flows
    private val _transactions = MutableStateFlow(initialTransactions)
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _alerts = MutableStateFlow(initialAlerts)
    val alerts: StateFlow<List<SecurityAlert>> = _alerts.asStateFlow()

    private val _rules = MutableStateFlow(initialRules)
    val rules: StateFlow<List<FraudRule>> = _rules.asStateFlow()

    private val _proofs = MutableStateFlow(sampleProofs)
    val proofs: StateFlow<List<PaymentProofAnalysis>> = _proofs.asStateFlow()

    private val _urlScans = MutableStateFlow(sampleUrls)
    val urlScans: StateFlow<List<UrlScanResult>> = _urlScans.asStateFlow()

    private val _chartData = MutableStateFlow(chartPoints)
    val chartData: StateFlow<List<ActivityChartPoint>> = _chartData.asStateFlow()

    // Sensitivity and sandbox settings
    private val _fraudThreshold = MutableStateFlow(65f)
    val fraudThreshold: StateFlow<Float> = _fraudThreshold.asStateFlow()

    private val _isSandboxActive = MutableStateFlow(true)
    val isSandboxActive: StateFlow<Boolean> = _isSandboxActive.asStateFlow()

    private val _autoBlockEnabled = MutableStateFlow(true)
    val autoBlockEnabled: StateFlow<Boolean> = _autoBlockEnabled.asStateFlow()

    // AI Assistant Chat Messages
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = "msg-1",
                isUser = false,
                text = "Welcome to PayPal AI Guardian Threat Intel. I monitor sandbox transaction velocities, forensic payment proofs, homograph phishing attempts, and anomaly triggers. How can I assist your security audit today?",
                timestamp = "14:30 UTC"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun getKpiMetrics(): KpiMetrics {
        val list = _transactions.value
        val verified = list.count { it.status == TransactionStatus.VERIFIED }
        val suspicious = list.count { it.status == TransactionStatus.FLAGGED || it.riskLevel == RiskLevel.HIGH }
        val review = list.count { it.status == TransactionStatus.PENDING }
        val totalVolume = list.sumOf { it.amount }
        val preventedFraud = list.filter { it.status == TransactionStatus.BLOCKED }.sumOf { it.amount }
        return KpiMetrics(
            totalTransactions = list.size,
            verifiedPayments = verified,
            suspiciousCount = suspicious,
            reviewRequiredCount = review,
            totalVolumeUsd = totalVolume,
            preventedFraudUsd = preventedFraud
        )
    }

    fun toggleRule(ruleId: String) {
        _rules.update { current ->
            current.map { rule ->
                if (rule.id == ruleId) rule.copy(isEnabled = !rule.isEnabled) else rule
            }
        }
    }

    fun resolveAlert(alertId: String) {
        _alerts.update { current ->
            current.map { alert ->
                if (alert.id == alertId) alert.copy(isResolved = true) else alert
            }
        }
    }

    fun dismissAlert(alertId: String) {
        _alerts.update { current ->
            current.filterNot { it.id == alertId }
        }
    }

    fun setFraudThreshold(value: Float) {
        _fraudThreshold.value = value
    }

    fun toggleSandbox(active: Boolean) {
        _isSandboxActive.value = active
    }

    fun toggleAutoBlock(enabled: Boolean) {
        _autoBlockEnabled.value = enabled
    }

    fun scanUrl(inputUrl: String): UrlScanResult {
        val trimmed = inputUrl.trim()
        val isPhishing = trimmed.contains("paypaI", ignoreCase = true) ||
                trimmed.contains("verify", ignoreCase = true) ||
                trimmed.contains(".xyz", ignoreCase = true) ||
                trimmed.contains(".tk", ignoreCase = true) ||
                trimmed.startsWith("http://")

        val result = if (isPhishing) {
            UrlScanResult(
                id = "scan-${System.currentTimeMillis()}",
                targetUrl = trimmed,
                domain = trimmed.substringAfter("://").substringBefore("/"),
                ipAddress = "194.38.20.14",
                sslValid = trimmed.startsWith("https://"),
                sslIssuer = if (trimmed.startsWith("https://")) "ZeroSSL Free CA" else "None (Cleartext)",
                domainAgeDays = 4,
                homographAttackDetected = trimmed.contains("paypaI", ignoreCase = false),
                typosquattingTarget = "PayPal Checkout Gateway",
                reputationScore = 12,
                classification = "High Risk Phishing Target",
                threatDetails = listOf(
                    "Domain similarity score 94% with official PayPal brand",
                    "Suspicious domain registrar with anonymous WHOIS guard",
                    "Credential capture heuristics detected in HTML payload"
                ),
                scanTimestamp = "Just now"
            )
        } else {
            UrlScanResult(
                id = "scan-${System.currentTimeMillis()}",
                targetUrl = trimmed,
                domain = trimmed.substringAfter("://").substringBefore("/").ifEmpty { "sandbox.paypal.com" },
                ipAddress = "151.101.65.140",
                sslValid = true,
                sslIssuer = "DigiCert High Assurance EV CA",
                domainAgeDays = 4200,
                homographAttackDetected = false,
                typosquattingTarget = null,
                reputationScore = 96,
                classification = "Verified Legitimate Gateway",
                threatDetails = listOf(
                    "High reputation score across threat intelligence feeds",
                    "Cryptographic certificate match verified",
                    "No malicious redirect scripts detected"
                ),
                scanTimestamp = "Just now"
            )
        }

        _urlScans.update { listOf(result) + it }
        return result
    }

    fun analyzePaymentProof(proofName: String, reportedAmount: String): PaymentProofAnalysis {
        val isTampered = proofName.contains("Forged", ignoreCase = true) ||
                proofName.contains("Altered", ignoreCase = true) ||
                reportedAmount.contains("12,500")

        val newProof = PaymentProofAnalysis(
            id = "PRF-${System.currentTimeMillis().toString().takeLast(4)}",
            sampleName = proofName,
            documentType = "User Uploaded Payment Screenshot (Demo Analysis)",
            reportedAmount = reportedAmount,
            reportedTimestamp = "Analyzed at " + SimpleDateFormat("HH:mm:ss 'UTC'", Locale.US).format(Date()),
            fontKerningScore = if (isTampered) 34 else 96,
            recipientMatch = !isTampered,
            tamperScore = if (isTampered) 88 else 6,
            verdict = if (isTampered) "MANIPULATED" else "AUTHENTIC",
            detectedAnomalies = if (isTampered) listOf(
                "Font weight discrepancy detected around numeric values",
                "Quantization noise mismatch between background and dollar stamp",
                "Missing PayPal cryptographic transaction seal watermark"
            ) else listOf(
                "Clean font baseline geometry across all currency rows",
                "Ledger hash verified against simulated sandbox ledger",
                "Timestamp aligns with simulated gateway log"
            ),
            digitalSignatureValid = !isTampered
        )
        _proofs.update { listOf(newProof) + it }
        return newProof
    }

    fun sendChatMessage(userText: String) {
        val userMsg = ChatMessage(
            id = "msg-${System.currentTimeMillis()}",
            isUser = true,
            text = userText,
            timestamp = SimpleDateFormat("HH:mm 'UTC'", Locale.US).format(Date())
        )
        _chatMessages.update { it + userMsg }

        // AI Guardian intelligent cybersecurity response
        val lower = userText.lowercase(Locale.US)
        val reply = when {
            lower.contains("txn-8921") || lower.contains("tor") ->
                "**Incident Analysis for TXN-8921-X9:**\n• **Severity:** High Risk (Score 88/100)\n• **Threat Vector:** Connection originated from known Tor Exit Relay `185.220.101.44` (Frankfurt, DE).\n• **Velocity Anomaly:** Prior login occurred in New York 22 minutes prior (impossible physical travel).\n• **Recommended Action:** Block payout settlement and initiate secondary multi-factor verification."

            lower.contains("homograph") || lower.contains("phishing") ->
                "**Homograph Phishing Defense:**\nHomograph attacks replace Latin ASCII characters with visually identical Unicode Cyrillic glyphs (e.g., Cyrillic 'а' U+0430 for Latin 'a' U+0061). PayPal AI Guardian uses punycode decoding and script mixing entropy analysis to flag homograph URLs before sandbox credentials can be compromised."

            lower.contains("proof") || lower.contains("ocr") || lower.contains("kerning") ->
                "**Payment Proof Forensic Verification:**\nOur OCR module checks:\n1. **Kerning Consistency:** Digit font spacing variances indicate manual photoshop editing.\n2. **Error Level Analysis (ELA):** Detects localized JPEG re-compression around the total amount field.\n3. **Ledger Cross-Check:** Matches transaction references against sandbox authorization logs."

            lower.contains("card testing") || lower.contains("bin") ->
                "**Card Testing Mitigation:**\nAutomated bot clusters often run low-value $1 authorizations across consecutive BIN sequences. Our Velocity Guard tracks device fingerprints and throttles requests exceeding 3 unique cards per 5-minute interval."

            else ->
                "**Intelligent Payment Guardian Analysis:**\nI have reviewed your query regarding sandbox cybersecurity posture. Current system status: **Normal Alert Level**, 5 active threat rules, 1 blocked bot attack in the last 4 hours. You can test our URL scanner, audit recent transactions, or simulate a webhook attack from the Settings panel."
        }

        val aiMsg = ChatMessage(
            id = "msg-ai-${System.currentTimeMillis()}",
            isUser = false,
            text = reply,
            timestamp = SimpleDateFormat("HH:mm 'UTC'", Locale.US).format(Date())
        )
        _chatMessages.update { it + aiMsg }
    }

    fun simulateAttackTrigger() {
        val simTxn = Transaction(
            id = "txn-sim-${System.currentTimeMillis()}",
            reference = "TXN-SIM-" + (1000..9999).random(),
            amount = 4999.00,
            currency = "USD",
            senderName = "Automated Adversary Probe",
            senderEmail = "botnet@adversary-proxy.org",
            recipientName = "Sandbox High-Value Merchant",
            recipientEmail = "merchant@sandbox.internal",
            timestamp = "Just now",
            status = TransactionStatus.BLOCKED,
            riskLevel = RiskLevel.CRITICAL,
            riskScore = 99,
            paymentMethod = "Compromised Sandbox Card •••• 9102",
            ipAddress = "195.123.210.8 (Bulletproof Proxy)",
            location = "Bucharest, RO",
            deviceFingerprint = "dev_fp_synthetic_emulator",
            fraudSignals = listOf(
                "SIMULATED ATTACK: Rapid credential spraying cluster",
                "Known adversary proxy IP subnet",
                "Automated rule 'Tor & Anonymizing Proxy Blocker' triggered"
            ),
            isSandbox = true
        )

        val simAlert = SecurityAlert(
            id = "alt-sim-${System.currentTimeMillis()}",
            title = "SIMULATED: Automated Botnet Spray Intercepted",
            description = "Simulated adversarial transaction (${simTxn.reference}) was intercepted by AI Guardian rules.",
            severity = RiskLevel.CRITICAL,
            timestamp = "Just now",
            affectedRef = simTxn.reference,
            category = "Simulated Attack"
        )

        _transactions.update { listOf(simTxn) + it }
        _alerts.update { listOf(simAlert) + it }
    }

    fun resetDemoData() {
        _transactions.value = initialTransactions
        _alerts.value = initialAlerts
        _rules.value = initialRules
        _proofs.value = sampleProofs
        _urlScans.value = sampleUrls
        _fraudThreshold.value = 65f
    }
}
