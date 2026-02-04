package boond.customeraccountlist.internal

import boond.customeraccountlist.CustomerAccountListReadModel
import boond.customeraccountlist.CustomerAccountListReadModelQuery
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238324
*/
@RestController
class CustomeraccountlistResource(private var queryGateway: QueryGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @GetMapping("/customeraccountlist")
  fun findReadModel(): CompletableFuture<CustomerAccountListReadModel> {
    return queryGateway.query(
        CustomerAccountListReadModelQuery(), CustomerAccountListReadModel::class.java)
  }
}
