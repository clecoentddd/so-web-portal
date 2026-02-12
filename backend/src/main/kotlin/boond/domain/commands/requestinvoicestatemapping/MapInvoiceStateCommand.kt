package boond.domain.commands.requestinvoicestatemapping

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659017958328
*/
data class MapInvoiceStateCommand(
    @TargetAggregateIdentifier var settingsId: UUID,
    var connectionId: UUID,
) : Command
