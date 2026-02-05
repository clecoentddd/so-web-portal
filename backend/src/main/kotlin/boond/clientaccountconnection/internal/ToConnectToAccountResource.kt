package boond.clientaccountconnection.internal

import boond.domain.commands.clientaccountconnection.ToConnectToAccountCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.*

/** Updated Payload: Removed companyId as it is resolved by the Aggregate state. */
data class ClientAccountConnectionPayload(val clientEmail: String)

@RestController
class ToConnectToAccountResource(private val commandGateway: CommandGateway) {

  private val logger = KotlinLogging.logger {}

  /** Debug endpoint: Also cleaned to match the requirement. */
  @CrossOrigin
  @PostMapping("/debug/clientaccountconnection")
  fun processDebugCommand(
      @RequestParam clientEmail: String,
      @RequestParam customerId: UUID
  ): CompletableFuture<Any> {
    logger.info { "Debug connection request for customer: $customerId" }
    // Note: You may need to pass 0L or update the Command definition to make companyId optional
    return commandGateway.send(
        ToConnectToAccountCommand(clientEmail = clientEmail, customerId = customerId))
  }

  /** Standard endpoint: Uses PathVariable for ID and Body for Email. */
  @CrossOrigin
  @PostMapping("/clientaccountconnection/{id}")
  fun processCommand(
      @PathVariable("id") customerId: UUID,
      @RequestBody payload: ClientAccountConnectionPayload
  ): CompletableFuture<Any> {
    logger.info { "Processing connection for customer: $customerId" }

    return commandGateway.send(
        ToConnectToAccountCommand(clientEmail = payload.clientEmail, customerId = customerId))
  }
}
