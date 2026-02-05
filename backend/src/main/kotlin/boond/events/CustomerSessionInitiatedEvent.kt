package boond.events

import boond.common.Event
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238234
*/
data class CustomerSessionInitiatedEvent(
    var sessionId: UUID,
    var companyId: Long,
    var customerId: UUID
) : Event
