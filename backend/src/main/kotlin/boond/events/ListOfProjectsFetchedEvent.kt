package boond.events

import boond.common.Event
import boond.domain.models.ProjectInfo
import java.util.UUID
import kotlin.collections.List

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238236
*/
data class ListOfProjectsFetchedEvent(
        var companyId: Long,
        var customerId: UUID,
        var projectList: List<ProjectInfo>,
        var sessionId: UUID
) : Event
