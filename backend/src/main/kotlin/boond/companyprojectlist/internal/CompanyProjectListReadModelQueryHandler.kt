package boond.companyprojectlist.internal

import boond.common.Event
import boond.common.QueryHandler
import boond.companyprojectlist.CompanyProjectListReadModel
import boond.companyprojectlist.CompanyProjectListReadModelQuery
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238316
*/
@Component
class CompanyProjectListReadModelQueryHandler(val eventStore: EventStore) :
    QueryHandler<CompanyProjectListReadModelQuery, CompanyProjectListReadModel> {

  @org.axonframework.queryhandling.QueryHandler
  override fun handleQuery(query: CompanyProjectListReadModelQuery): CompanyProjectListReadModel {
    val events =
        eventStore
            .readEvents(query.sessionId.toString())
            .asStream()
            .filter { it.payload is Event }
            .map { it.payload as Event }
            .toList()

    return CompanyProjectListReadModel().applyEvents(events)
  }
}
