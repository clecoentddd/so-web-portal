package boond.domain.commands.initiatecustomersession

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238327
*/
data class InitiateSessionCommand(
    @TargetAggregateIdentifier var sessionId: UUID,
    var companyId: Long,
    var customerId: UUID
) : Command
