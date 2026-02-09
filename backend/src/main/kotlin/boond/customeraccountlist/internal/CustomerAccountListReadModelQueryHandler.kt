package boond.customeraccountlist.internal

import boond.customeraccountlist.CustomerAccountListReadModel
import boond.customeraccountlist.CustomerAccountListReadModelQuery
import boond.customeraccountlist.CustomerAccountLookupQuery
import boond.customeraccountlist.CustomerAccountLookupReadModel
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238324
*/

@Component
class CustomerAccountListReadModelQueryHandler(
    private val repository: CustomerAccountListReadModelRepository
) {

  /** Handles the request for the full list of accounts. */
  @QueryHandler
  fun handleQuery(query: CustomerAccountListReadModelQuery): CustomerAccountListReadModel? {
    return CustomerAccountListReadModel(repository.findAll())
  }

  /**
   * Handles the specific discovery lookup for a single customer. This allows the frontend to
   * resolve the companyId based on the customerId.
   */
  @QueryHandler
  fun handleLookup(query: CustomerAccountLookupQuery): CustomerAccountLookupReadModel? {
    // We find the specific entity from the database
    val entity = repository.findByCustomerId(query.customerId)

    // Map the database entity to the ReadModel DTO
    return entity?.let {
      CustomerAccountLookupReadModel(
          customerId = it.customerId!!, companyId = it.companyId!!, companyName = it.companyName!!)
    }
  }
}
