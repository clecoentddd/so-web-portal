package boond.listofcompanies

import boond.common.CompanyInfo
import boond.common.Event
import boond.common.Query
import boond.common.ReadModel
import boond.events.ListOfCompaniesFetchedEvent
import java.util.UUID
import kotlin.collections.List

class ListOfCompaniesReadModelQuery(var connectionId: UUID) : Query

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238268
*/
class ListOfCompaniesReadModel : ReadModel {

   var connectionId: UUID? = null
   var adminEmail: String? = null
   var listOfCompanies: MutableList<CompanyInfo> = mutableListOf()

   fun applyEvents(events: List<Event>): ListOfCompaniesReadModel {

      events.forEach { event ->
         when (event) {
            is ListOfCompaniesFetchedEvent -> {
               connectionId = event.connectionId
               adminEmail = event.adminEmail
               listOfCompanies = event.listOfCompanies.toMutableList()
            }
         }
      }

      return this
   }
}
