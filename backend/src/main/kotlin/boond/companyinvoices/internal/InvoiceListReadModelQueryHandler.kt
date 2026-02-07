package boond.companyinvoices.internal

import boond.companyinvoices.InvoiceListReadModel
import boond.companyinvoices.InvoiceListReadModelQuery
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class InvoiceListReadModelQueryHandler(private val repository: InvoiceListReadModelRepository) {

  @QueryHandler
  fun handle(query: InvoiceListReadModelQuery): InvoiceListReadModel? {
    return repository.findById(query.sessionId).map { InvoiceListReadModel(it) }.orElse(null)
  }
}
