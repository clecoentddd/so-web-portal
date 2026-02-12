package boond.invoicestatemapping.internal

import boond.invoicestatemapping.InvoiceStateMappingProjectionEntity
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

/** Query object for fetching all invoice state mappings */
class InvoiceStateMappingProjectionQuery // no fields, because settingsId is fixed

@Component
class InvoiceStateMappingQueryHandler(
    private val repository: InvoiceStateMappingProjectionRepository
) {

  @QueryHandler
  fun handle(query: InvoiceStateMappingProjectionQuery): List<InvoiceStateMappingProjectionEntity> {
    // There is only one settingsId, so just fetch everything
    return repository.findAll()
  }
}
