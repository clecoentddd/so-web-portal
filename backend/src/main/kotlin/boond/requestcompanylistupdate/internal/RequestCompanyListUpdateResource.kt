package boond.requestcompanylistupdate.internal

import boond.domain.commands.requestcompanylistupdate.RequestCompanyListUpdateCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class RequestCompanyListUpdatePayload(var settingsId: UUID, var connectionId: UUID)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659433747639
*/
@RestController
class RequestCompanyListUpdateResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/debug/requestcompanylistupdate")
  fun processDebugCommand(
      @RequestParam settingsId: UUID,
      @RequestParam connectionId: UUID
  ): CompletableFuture<Any> {
    return commandGateway.send(RequestCompanyListUpdateCommand(settingsId, connectionId))
  }

  @CrossOrigin
  @PostMapping("/requestcompanylistupdate/{settingsId}")
  fun processCommand(
      @PathVariable("settingsId") settingsId: UUID,
      @RequestParam connectionId: UUID
  ): CompletableFuture<Any> {
    return commandGateway.send(
        RequestCompanyListUpdateCommand(settingsId = settingsId, connectionId = connectionId))
  }
}
