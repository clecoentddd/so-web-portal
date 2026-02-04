package boond.listofcompanies.internal

import boond.listofcompanies.ListOfCompaniesReadModel
import boond.listofcompanies.ListOfCompaniesReadModelQuery
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238268
*/
@RestController
class ListofcompaniesResource(private var queryGateway: QueryGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @GetMapping("/listofcompanies/{id}")
  fun findReadModel(
      @PathVariable("id") connectionId: UUID
  ): CompletableFuture<ListOfCompaniesReadModel> {
    return queryGateway.query(
        ListOfCompaniesReadModelQuery(connectionId), ListOfCompaniesReadModel::class.java)
  }
}
