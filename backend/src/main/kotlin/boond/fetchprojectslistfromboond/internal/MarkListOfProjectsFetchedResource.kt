package boond.fetchprojectslistfromboond.internal

import boond.domain.commands.fetchprojectslistfromboond.MarkListOfProjectsFetchedCommand
import boond.domain.models.ProjectInfo
import java.util.UUID
import java.util.concurrent.CompletableFuture
import kotlin.collections.List
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class FetchProjectsListFromBoondPayload(
    var sessionId: UUID,
    var companyId: Long,
    var customerId: UUID,
    var projectList: List<ProjectInfo> = emptyList()
)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238304
*/
@RestController
class MarkListOfProjectsFetchedResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/debug/fetchprojectslistfromboond")
  fun processDebugCommand(
      @RequestParam sessionId: UUID,
      @RequestParam companyId: Long,
      @RequestParam customerId: UUID,
      @RequestParam projectList: List<ProjectInfo>
  ): CompletableFuture<Any> {
    return commandGateway.send(
        MarkListOfProjectsFetchedCommand(sessionId, companyId, customerId, projectList))
  }

  @CrossOrigin
  @PostMapping("/fetchprojectslistfromboond/{id}")
  fun processCommand(
      @PathVariable("id") sessionId: UUID,
      @RequestBody payload: FetchProjectsListFromBoondPayload
  ): CompletableFuture<Any> {
    return commandGateway.send(
        MarkListOfProjectsFetchedCommand(
            sessionId = payload.sessionId,
            companyId = payload.companyId,
            customerId = payload.customerId,
            projectList = payload.projectList))
  }
}
