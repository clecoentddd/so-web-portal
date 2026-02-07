package boond.requestprojectdetails.internal

import boond.domain.commands.requestprojectdetails.RequestProjectDetailsCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class RequestProjectDetailsPayload(
    var companyId: Long,
    var customerId: UUID,
    var projectId: Long
)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238353
*/
@RestController
class RequestProjectDetailsResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/debug/requestprojectdetails")
  fun processDebugCommand(
      @RequestParam companyId: Long,
      @RequestParam customerId: UUID,
      @RequestParam projectId: Long,
      @RequestParam sessionId: UUID
  ): CompletableFuture<Any> {
    return commandGateway.send(
        RequestProjectDetailsCommand(companyId, customerId, projectId, sessionId))
  }

  @CrossOrigin
  @PostMapping("/requestprojectdetails/{id}")
  fun processCommand(
      @PathVariable("id") sessionId: UUID,
      @RequestBody payload: RequestProjectDetailsPayload
  ): CompletableFuture<Any> {
    return commandGateway.send(
        RequestProjectDetailsCommand(
            companyId = payload.companyId,
            customerId = payload.customerId,
            projectId = payload.projectId,
            sessionId = sessionId))
  }
}
