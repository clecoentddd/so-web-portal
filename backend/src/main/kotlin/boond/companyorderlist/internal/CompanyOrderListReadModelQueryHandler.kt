package boond.companyorderlist.internal

import boond.companyorderlist.CompanyOrderListReadModel
import boond.companyorderlist.CompanyOrderListReadModelQuery
import boond.companyorderlist.ProjectOrdersQuery
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238377
*/
@Component
class CompanyOrderListQueryHandler(private val repository: CompanyOrderListReadModelRepository) {

  @QueryHandler
  fun handleQuery(query: CompanyOrderListReadModelQuery): CompanyOrderListReadModel? {
    val entityOptional = repository.findById(query.sessionId)
    if (entityOptional.isEmpty) {
      return null
    }

    return CompanyOrderListReadModel(entityOptional.get())
  }

  @QueryHandler
  fun handleProjectOrders(query: ProjectOrdersQuery): CompanyOrderListReadModel? {
    val entityOptional = repository.findById(query.sessionId)
    if (entityOptional.isEmpty) {
      return null
    }

    val readModel = CompanyOrderListReadModel(entityOptional.get())

    // Filter the list to only include orders for the specific projectId
    readModel.orderList =
        readModel.orderList.filter { it.projectId == query.projectId }.toMutableList()

    return readModel
  }
}
