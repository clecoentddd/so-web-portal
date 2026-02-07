package boond.events

import boond.common.Event
import java.util.UUID

data class OrderRecordedEvent(
    val sessionId: UUID,
    val orderId: Long,
    val reference: String,
    val title: String,
    val state: Int,
    val orderDate: String,
    val startDate: String,
    val endDate: String,
    val totalExcludingTaxes: Double,
    val totalIncludingTaxes: Double,
    val totalVat: Double,
    val companyId: Long,
    val projectReference: String
) : Event
