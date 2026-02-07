package boond.companyorderlist.internal

import boond.companyorderlist.CompanyOrderListReadModel
import boond.companyorderlist.CompanyOrderListReadModelQuery
import boond.companyorderlist.ProjectOrdersQuery
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238377
*/

@RestController
class CompanyorderlistResource(private val queryGateway: QueryGateway) {

  private val logger = KotlinLogging.logger {}

  @CrossOrigin
  @GetMapping("/companyorderlist/{id}")
  fun findReadModel(
      @PathVariable("id") sessionId: UUID
  ): CompletableFuture<CompanyOrderListReadModel> {
    logger.info { "Fetching full order list for session: $sessionId" }
    return queryGateway.query(
        CompanyOrderListReadModelQuery(sessionId), CompanyOrderListReadModel::class.java)
  }

  @CrossOrigin
  @GetMapping("/companyorderlist/{id}/project/{projectId}")
  fun findByProject(
      @PathVariable("id") sessionId: UUID,
      @PathVariable("projectId") projectId: Long,
      @RequestParam("companyId") companyId: Long
  ): CompletableFuture<CompanyOrderListReadModel> {
    logger.info { "Fetching orders for session: $sessionId, project: $projectId" }
    return queryGateway.query(
        ProjectOrdersQuery(sessionId, projectId, companyId), CompanyOrderListReadModel::class.java)
  }
}
