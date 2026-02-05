package boond.domain

import boond.domain.commands.initiatecustomersession.InitiateSessionCommand
import boond.events.CustomerSessionInitiatedEvent
import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class SessionAggregate() {

  @AggregateIdentifier private lateinit var sessionId: UUID
  private var isActive: Boolean = false

  @CommandHandler
  @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
  fun handle(command: InitiateSessionCommand) {
    // Semantic validation can happen here if needed

    AggregateLifecycle.apply(
        CustomerSessionInitiatedEvent(
            sessionId = command.sessionId,
            companyId = command.companyId,
            customerId = command.customerId))
  }

  @EventSourcingHandler
  fun on(event: CustomerSessionInitiatedEvent) {
    this.sessionId = event.sessionId
    this.isActive = true
  }
}
