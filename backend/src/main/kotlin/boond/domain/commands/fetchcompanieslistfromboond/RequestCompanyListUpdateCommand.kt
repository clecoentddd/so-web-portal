package boond.domain.commands.fetchcompanieslistfromboond

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RequestCompanyListUpdateCommand(
    @TargetAggregateIdentifier val settingsId: UUID,
    val connectionId: UUID
) : Command
