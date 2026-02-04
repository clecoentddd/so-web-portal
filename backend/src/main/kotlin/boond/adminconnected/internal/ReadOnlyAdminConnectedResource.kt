package boond.adminconnected.internal

import boond.adminconnected.AdminConnectedReadModel
import boond.adminconnected.AdminConnectedReadModelQuery
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238263
*/
@RestController
class AdminconnectedResource(private var queryGateway: QueryGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @GetMapping("/adminconnected/{id}")
  fun findReadModel(
      @PathVariable("id") connectionId: UUID
  ): CompletableFuture<AdminConnectedReadModel> {
    return queryGateway.query(
        AdminConnectedReadModelQuery(connectionId), AdminConnectedReadModel::class.java)
  }
}
