package boond.events

import boond.common.Event
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238237
*/
data class ProjectDetailsRequestedEvent(
    var sessionId: UUID,
    var companyId: Long,
    var customerId: UUID,
    var projectId: String
) : Event
