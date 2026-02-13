package boond.events

import boond.common.Event
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659017475085
*/
data class InvoiceStateMappingUpdateRequestedEvent(var settingsId: UUID, var connectionId: UUID) :
    Event
