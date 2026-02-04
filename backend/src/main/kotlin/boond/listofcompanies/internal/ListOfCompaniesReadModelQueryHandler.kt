package boond.listofcompanies.internal

import boond.common.Event
import boond.common.QueryHandler
import boond.listofcompanies.ListOfCompaniesReadModel
import boond.listofcompanies.ListOfCompaniesReadModelQuery
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238268
*/
@Component
class ListOfCompaniesReadModelQueryHandler(val eventStore: EventStore) :
    QueryHandler<ListOfCompaniesReadModelQuery, ListOfCompaniesReadModel> {

  @org.axonframework.queryhandling.QueryHandler
  override fun handleQuery(query: ListOfCompaniesReadModelQuery): ListOfCompaniesReadModel {
    val events =
        eventStore
            .readEvents(query.connectionId.toString())
            .asStream()
            .filter { it.payload is Event }
            .map { it.payload as Event }
            .toList()

    return ListOfCompaniesReadModel().applyEvents(events)
  }
}
