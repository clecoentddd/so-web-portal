package boond.initiatecustomersession.internal

import boond.common.Processor
import boond.domain.commands.initiatecustomersession.InitiateSessionCommand
import boond.events.CustomerConnectedEvent
import java.util.UUID
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class SessionProcessorProcessor(private val commandGateway: CommandGateway) : Processor {

  private val logger = KotlinLogging.logger {}

  @EventHandler
  fun on(event: CustomerConnectedEvent) {
    // 1. Business Logic Check
    // Since the compiler guarantees customerId is not null, we focus on the value validity
    if (event.companyId <= 0) {
      logger.error {
        "Aborting: Received invalid companyId (${event.companyId}) for customer ${event.customerId}"
      }
      return
    }

    // 2. Generate the SessionId prior to calling the command
    val newSessionId = UUID.randomUUID()

    logger.info {
      "Handshaking: Customer ${event.customerId} (Company: ${event.companyId}) -> New Session: $newSessionId"
    }

    // 3. Dispatch the command to the Session Aggregate
    commandGateway
        .send<Any>(
            InitiateSessionCommand(
                sessionId = newSessionId,
                companyId = event.companyId,
                customerId = event.customerId))
        .exceptionally { throwable ->
          logger.error(throwable) { "Command failed: Could not initiate session $newSessionId" }
          null
        }
  }
}
