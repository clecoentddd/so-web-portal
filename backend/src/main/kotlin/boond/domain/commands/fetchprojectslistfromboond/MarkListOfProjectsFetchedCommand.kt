package boond.domain.commands.fetchprojectslistfromboond

import boond.common.Command
import boond.domain.models.ProjectInfo
import java.util.UUID
import kotlin.collections.List
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238304
*/
data class MarkListOfProjectsFetchedCommand(
    @TargetAggregateIdentifier var sessionId: UUID,
    var companyId: Long,
    var customerId: UUID,
    var projectList: List<ProjectInfo> = emptyList()
) : Command
