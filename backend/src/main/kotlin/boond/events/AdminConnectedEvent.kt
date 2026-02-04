package boond.events

import boond.common.Event
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238222
*/
data class AdminConnectedEvent(var connectionId: UUID, var adminEmail: String) : Event
