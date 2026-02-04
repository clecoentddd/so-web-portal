package boond.customeraccountlist.internal

import boond.customeraccountlist.CustomerAccountListReadModel
import boond.customeraccountlist.CustomerAccountListReadModelQuery
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238324
*/
@Component
class CustomerAccountListReadModelQueryHandler(
    private val repository: CustomerAccountListReadModelRepository
) {

  @QueryHandler
  fun handleQuery(query: CustomerAccountListReadModelQuery): CustomerAccountListReadModel? {
    return CustomerAccountListReadModel(repository.findAll())
  }
}
