package boond.customersessions.internal

/* Other imports */
import boond.customersessions.CustomerSessionsReadModel
import boond.customersessions.CustomerSessionsReadModelQuery
import java.util.UUID
import java.util.concurrent.CompletableFuture
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

@RestController
class CustomersessionsResource(private val queryGateway: QueryGateway) {

  /**
   * DISCOVERY: Frontend calls this with customerId and companyId to find the sessionId after login.
   */
  @CrossOrigin
  @GetMapping("/customersessions/lookup/{customerId}/{companyId}")
  fun lookup(
      @PathVariable customerId: UUID,
      @PathVariable companyId: Long
  ): CompletableFuture<CustomerSessionsReadModel> {
    return queryGateway.query(
        FindSessionByCustomerAndCompanyQuery(customerId, companyId),
        CustomerSessionsReadModel::class.java)
  }

  /** FETCH: Standard fetch by sessionId. */
  @CrossOrigin
  @GetMapping("/customersessions/{id}")
  fun findReadModel(@PathVariable id: UUID): CompletableFuture<CustomerSessionsReadModel> {
    return queryGateway.query(
        CustomerSessionsReadModelQuery(id), CustomerSessionsReadModel::class.java)
  }
}
