package boond.customersessions.internal

import boond.customersessions.CustomerSessionsReadModel
import boond.customersessions.CustomerSessionsReadModelQuery
import java.util.UUID
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

// Define the NEW lookup query here so the Resource can import it
class FindSessionByCustomerAndCompanyQuery(val customerId: UUID, val companyId: Long)

@Component
class CustomerSessionsReadModelQueryHandler(
    private val repository: CustomerSessionsReadModelRepository
) {

  // Handler for looking up by sessionId (Standard)
  @QueryHandler
  fun handleQuery(query: CustomerSessionsReadModelQuery): CustomerSessionsReadModel? {
    val entity = repository.findById(query.sessionId).orElse(null) ?: return null
    return CustomerSessionsReadModel(entity)
  }

  // Handler for looking up by Customer + Company (Discovery)
  @QueryHandler
  fun handleLookup(query: FindSessionByCustomerAndCompanyQuery): CustomerSessionsReadModel? {
    val entity =
        repository.findByCustomerIdAndCompanyId(query.customerId, query.companyId).orElse(null)
            ?: return null
    return CustomerSessionsReadModel(entity)
  }
}
