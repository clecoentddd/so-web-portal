package boond.customeraccountlist.internal

import boond.customeraccountlist.CustomerAccountListReadModel
import boond.customeraccountlist.CustomerAccountListReadModelQuery
import boond.customeraccountlist.CustomerAccountLookupQuery
import boond.customeraccountlist.CustomerAccountLookupReadModel
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238324
*/
@RestController
class CustomeraccountlistResource(private var queryGateway: QueryGateway) {

  private var logger = KotlinLogging.logger {}

  /** Returns the full list of customer accounts. */
  @CrossOrigin
  @GetMapping("/customeraccountlist")
  fun findReadModel(): CompletableFuture<CustomerAccountListReadModel> {
    logger.info { "Fetching full customer account list" }
    return queryGateway.query(
        CustomerAccountListReadModelQuery(), CustomerAccountListReadModel::class.java)
  }

  /**
   * Specifically look up a single customer's account details (to find their companyId). Used by the
   * frontend during login discovery.
   */
  @CrossOrigin
  @GetMapping("/customeraccountlookup/{customerId}")
  fun lookupCustomerAccount(
      @PathVariable("customerId") customerId: UUID
  ): CompletableFuture<CustomerAccountLookupReadModel> {
    logger.info { "Looking up account details for customer: $customerId" }
    return queryGateway.query(
        CustomerAccountLookupQuery(customerId), CustomerAccountLookupReadModel::class.java)
  }
}
