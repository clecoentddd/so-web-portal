package boond.companyprojectlist.internal

import boond.companyprojectlist.CompanyProjectListReadModel
import boond.companyprojectlist.CompanyProjectListReadModelQuery
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238316
*/
@RestController
class CompanyprojectlistResource(private var queryGateway: QueryGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @GetMapping("/companyprojectlist/{id}")
  fun findReadModel(
      @PathVariable("id") sessionId: UUID
  ): CompletableFuture<CompanyProjectListReadModel> {
    return queryGateway.query(
        CompanyProjectListReadModelQuery(sessionId), CompanyProjectListReadModel::class.java)
  }
}
