package boond.domain.commands.fetchinvoicestatemapping

import boond.common.Command
import boond.common.InvoiceStateMappingInfo
import java.util.UUID
import kotlin.collections.List
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659018058978
*/

/* I am not using TargetAggregateIdentifier because this command does not target a specific aggregate   */

data class MarkInvoiceStateMappingFetchedCommand(
        @TargetAggregateIdentifier var settingsId: UUID,
        var connectionId: UUID,
        var invoiceStateMapping: List<InvoiceStateMappingInfo>
) : Command
