package boond.initializesettings.internal

import boond.domain.commands.initializesettings.InitializeSettingsCommand
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

data class InitializeSettingsPayload(var settingsId: UUID, var connectionId: UUID)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659095873109
*/
@RestController
class InitializeSettingsResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/debug/initializesettings")
  fun processDebugCommand(
      @RequestParam settingsId: UUID,
      @RequestParam connectionId: UUID
  ): CompletableFuture<Any> {
    return commandGateway.send(InitializeSettingsCommand(settingsId, connectionId))
  }

  @CrossOrigin
  @PostMapping("/initializesettings/{id}")
  fun processCommand(
      @PathVariable("id") settingsId: UUID,
      @RequestBody payload: InitializeSettingsPayload
  ): CompletableFuture<Any> {
    return commandGateway.send(
        InitializeSettingsCommand(
            settingsId = payload.settingsId, connectionId = payload.connectionId))
  }
}
