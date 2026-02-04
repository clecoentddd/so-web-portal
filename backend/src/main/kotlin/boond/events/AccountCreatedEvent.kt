package boond.events

import boond.common.Event
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238229
*/
data class AccountCreatedEvent(
    var connectionId: UUID,
    var clientEmail: String,
    var companyId: Long,
    var customerId: UUID
) : Event
