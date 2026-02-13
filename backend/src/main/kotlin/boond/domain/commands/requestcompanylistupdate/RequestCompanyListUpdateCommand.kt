package boond.domain.commands.requestcompanylistupdate

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659433747639
*/
data class RequestCompanyListUpdateCommand(
    @TargetAggregateIdentifier var settingsId: UUID,
    var connectionId: UUID
) : Command
