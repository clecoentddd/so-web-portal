package boond.domain.commands.adminconnection

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238252
*/

data class ToConnectCommand(
    @TargetAggregateIdentifier var connectionId: UUID,
    var adminEmail: String
) : Command
