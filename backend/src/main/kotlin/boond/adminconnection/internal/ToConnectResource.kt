package boond.adminconnection.internal

import boond.domain.commands.adminconnection.ToConnectCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class AdminConnectionPayload(var adminEmail: String)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238252
*/

@RestController
class ToConnectResource(private var commandGateway: CommandGateway) {

  private var logger = KotlinLogging.logger {}

  /**
   * PUBLIC ROUTE: Used by the Frontend Frontend sends: { "adminEmail": "user@example.com" } Backend
   * generates: UUID
   */
  @CrossOrigin
  @PostMapping("/adminconnection")
  fun processCommand(@RequestBody payload: AdminConnectionPayload): Map<String, String> {
    val generatedConnectionId = UUID.randomUUID()

    logger.info(
        "Generating new connection for ${payload.adminEmail} with ID $generatedConnectionId")

    commandGateway.sendAndWait<Any>(
        ToConnectCommand(connectionId = generatedConnectionId, adminEmail = payload.adminEmail))

    // Return the ID to the frontend so it knows which UUID was generated
    return mapOf("connectionId" to generatedConnectionId.toString())
  }

  /** DEBUG ROUTE: Only if you manually want to force a specific UUID in Swagger */
  @CrossOrigin
  @PostMapping("/debug/adminconnection")
  fun processDebugCommand(
      @RequestParam connectionId: UUID,
      @RequestParam adminEmail: String
  ): CompletableFuture<Any> {
    return commandGateway.send(ToConnectCommand(connectionId, adminEmail))
  }
}
