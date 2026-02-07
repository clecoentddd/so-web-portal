package boond.domain.commands.fetchinvoices

import boond.common.Command
import boond.common.InvoiceInfo
import java.util.UUID
import kotlin.collections.List
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238313
*/
data class MarkInvoicesFetchedCommand(
    @TargetAggregateIdentifier var sessionId: UUID,
    var companyId: Long,
    var customerId: UUID,
    var invoiceList: List<InvoiceInfo>
) : Command
