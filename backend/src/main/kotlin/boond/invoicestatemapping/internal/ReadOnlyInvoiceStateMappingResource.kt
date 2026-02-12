package boond.invoicestatemapping.internal

import boond.invoicestatemapping.InvoiceStateMappingProjectionEntity
import java.util.concurrent.CompletableFuture
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invoicestatemapping")
class InvoiceStateMappingResource(private val queryGateway: QueryGateway) {

  @CrossOrigin
  @GetMapping
  fun getAll(): CompletableFuture<List<InvoiceStateMappingProjectionEntity>> {
    return queryGateway.query(
        InvoiceStateMappingProjectionQuery(), // no settingsId needed
        ResponseTypes.multipleInstancesOf(InvoiceStateMappingProjectionEntity::class.java))
  }
}
