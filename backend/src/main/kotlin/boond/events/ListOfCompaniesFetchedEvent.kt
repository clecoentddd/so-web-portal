package boond.events

import boond.common.Event
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238224
*/

data class ListOfCompaniesFetchedEvent(
    val connectionId: UUID? = null,
    val adminEmail: String = "",
    val listOfCompanies: List<CompanyInfo> = emptyList()
) : Event

data class CompanyInfo(val companyId: Long, val companyName: String)
