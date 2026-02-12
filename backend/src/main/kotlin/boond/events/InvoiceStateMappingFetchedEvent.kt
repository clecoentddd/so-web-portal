package boond.events

import boond.common.Event
import java.util.UUID
import kotlin.collections.List

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659017837700
*/
data class InvoiceStateMappingFetchedEvent(
    var connectionId: UUID,
    var InvoiceStateMapping: List<String>,
    var settingsId: String
) : Event
