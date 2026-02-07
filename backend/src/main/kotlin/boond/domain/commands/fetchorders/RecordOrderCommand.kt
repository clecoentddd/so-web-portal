package boond.domain.commands.fetchorders

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RecordOrderCommand(
    @TargetAggregateIdentifier val sessionId: UUID,
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
) : Command
