package boond.domain.commands.requestprojectdetails

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238353
*/
data class RequestProjectDetailsCommand(
    var companyId: Long,
    var customerId: UUID,
    var projectId: Long,
    @TargetAggregateIdentifier var sessionId: UUID
) : Command {
  init {
    require(companyId != 0L) { "companyId is required" }
    require(customerId != UUID(0, 0)) { "customerId is required" }
    require(projectId != 0L) { "projectId is required" }
    require(sessionId != UUID(0, 0)) { "sessionId is required" }
  }
}
