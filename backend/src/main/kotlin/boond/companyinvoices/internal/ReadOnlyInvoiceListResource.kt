package boond.companyinvoices.internal

import boond.companyinvoices.InvoiceListReadModel
import boond.companyinvoices.InvoiceListReadModelQuery
import java.util.UUID
import java.util.concurrent.CompletableFuture
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

@RestController
class CompanyinvoicesResource(private val queryGateway: QueryGateway) {

     @CrossOrigin
     @GetMapping("/companyinvoices/{id}")
     fun findReadModel(
             @PathVariable("id") sessionId: UUID
     ): CompletableFuture<InvoiceListReadModel> {
          return queryGateway.query(
                  InvoiceListReadModelQuery(sessionId),
                  InvoiceListReadModel::class.java
          )
     }
}
