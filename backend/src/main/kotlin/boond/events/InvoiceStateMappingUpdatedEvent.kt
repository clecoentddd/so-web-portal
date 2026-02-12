package boond.events

import boond.common.Event
import boond.common.InvoiceStateMappingInfo
import java.util.UUID
import kotlin.collections.List

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659017837700
*/
data class InvoiceStateMappingUpdatedEvent(
        var settingsId: UUID,
        var connectionId: UUID,
        var invoiceStateMapping: List<InvoiceStateMappingInfo>
) : Event
