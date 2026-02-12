package boond.events

import boond.common.CompanyInfo
import boond.common.Event
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238224
*/

data class ListOfCompaniesFetchedEvent(
        var settingsId: UUID? = null,
        var connectionId: UUID? = null,
        var listOfCompanies: List<CompanyInfo> = emptyList()
) : Event
