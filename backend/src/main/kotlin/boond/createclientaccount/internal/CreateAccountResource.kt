package boond.createclientaccount.internal

import boond.domain.commands.createclientaccount.CreateAccountCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.MetaData
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class CreateClientAccountPayload(
    var connectionId: UUID,
    var clientEmail: String,
    var companyId: Long
)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238320
*/
@RestController
class CreateAccountResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/debug/createclientaccount")
  fun processDebugCommand(
      @RequestParam connectionId: UUID,
      @RequestParam clientEmail: String,
      @RequestParam companyId: Long,
      @RequestParam customerId: UUID
  ): CompletableFuture<Any> {
    return commandGateway.send(
        CreateAccountCommand(connectionId, clientEmail, companyId, customerId))
  }

  @CrossOrigin
  @PostMapping("/createclientaccount") // Removed trailing slash for standard REST
  fun processCommand(@RequestBody payload: CreateClientAccountPayload): Map<String, String> {

    // 1. Ensure companyId is set:
    requireNotNull(payload.companyId) { "The companyId is mandatory to create an account." }
    require(payload.companyId > 0) { "The companyId must be a valid positive ID." }

    // 2. Generate the ID here so we can return it to the caller
    val generatedCustomerId = UUID.randomUUID()

    // 3. Send the command.
    // We use send() or sendAndWait() depending on if we want to block.
    commandGateway.sendAndWait<Any>(
        CreateAccountCommand(
            customerId = generatedCustomerId,
            connectionId = payload.connectionId,
            clientEmail = payload.clientEmail,
            companyId = payload.companyId),
        MetaData.with("COMPANY_ID", payload.companyId))

    // 3. Return the ID so the UI can redirect to the new customer profile
    return mapOf("customerId" to generatedCustomerId.toString())
  }
}
