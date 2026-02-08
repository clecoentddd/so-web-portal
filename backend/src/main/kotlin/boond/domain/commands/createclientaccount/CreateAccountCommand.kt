package boond.domain.commands.createclientaccount

import boond.common.Command
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238320
*/
data class CreateAccountCommand(
        @TargetAggregateIdentifier @Schema(hidden = true) var customerId: UUID,
        var clientEmail: String,
        var companyId: Long,
        var companyName: String,
        var connectionId: UUID
) : Command
