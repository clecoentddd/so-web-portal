package boond.common

import jakarta.persistence.Embeddable

@Embeddable
data class InvoiceInfo(
    var invoiceId: Long,
    var companyId: Long,
    var projectId: Long,
    var orderId: Long,
    var reference: String,
    var title: String,
    var state: Int,
    var invoiceDate: String,
    var dueDate: String,
    var performedDate: String,
    // Flattened fields from "Informations"
    var totalExcludingTaxes: Double,
    var totalIncludingTaxes: Double,
    var totalVat: Double
)
