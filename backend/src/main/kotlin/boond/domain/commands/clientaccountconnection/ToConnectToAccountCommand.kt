package boond.domain.commands.customeraccountconnection

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238288
*/
data class ToConnectToAccountCommand(
    var clientEmail: String,
    @TargetAggregateIdentifier var customerId: UUID
) : Command
