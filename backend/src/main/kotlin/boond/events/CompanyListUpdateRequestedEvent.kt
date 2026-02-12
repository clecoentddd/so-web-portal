package boond.events

import boond.common.Event
import java.util.UUID

data class CompanyListUpdateRequestedEvent(val settingsId: UUID, val connectionId: UUID) : Event
