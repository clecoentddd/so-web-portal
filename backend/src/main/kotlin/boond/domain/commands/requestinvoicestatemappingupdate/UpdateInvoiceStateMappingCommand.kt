package boond.domain.commands.requestinvoicestatemappingupdate

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659017958328
*/
data class UpdateInvoiceStateMappingCommand(
        @TargetAggregateIdentifier var settingsId: UUID,
        var connectionId: UUID
) : Command
