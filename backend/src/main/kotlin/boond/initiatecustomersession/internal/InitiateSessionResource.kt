package boond.initiatecustomersession.internal

import boond.domain.commands.initiatecustomersession.InitiateSessionCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class InitiateCustomerSessionPayload(
    var sessionId: UUID,
    var companyId: Long,
    var customerId: UUID
)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238327
*/
@RestController
class InitiateSessionResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/initiatecustomersession")
  fun processCommand(
      @RequestBody payload: InitiateCustomerSessionPayload
  ): CompletableFuture<UUID> {

    // 1. Syntactic Validation: Fail fast before the Axon bus
    requireNotNull(payload.customerId) { "customerId must be provided" }
    requireNotNull(payload.companyId) { "companyId must be provided" }
    require(payload.companyId > 0) { "companyId must be a positive value" }

    // 2. ID Generation: The server creates the identity of the new Session Aggregate
    val generatedSessionId = UUID.randomUUID()

    logger.info { "Initiating session $generatedSessionId for customer: ${payload.customerId}" }

    // 3. Dispatch and return the ID to the client
    return commandGateway
        .send<Any>(
            InitiateSessionCommand(
                sessionId = generatedSessionId,
                companyId = payload.companyId,
                customerId = payload.customerId))
        .thenApply { generatedSessionId }
  }
}
