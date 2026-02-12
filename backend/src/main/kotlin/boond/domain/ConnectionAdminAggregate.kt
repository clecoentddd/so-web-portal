package boond.domain

import boond.domain.commands.adminconnection.ToConnectCommand
import boond.events.AdminConnectedEvent
import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ConnectionAdminAggregate {

  @AggregateIdentifier var connectionId: java.util.UUID? = null

  @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
  @CommandHandler
  fun handle(command: ToConnectCommand) {
    require(command.adminEmail.isNotBlank()) { "Admin email must not be empty" }

    AggregateLifecycle.apply(
            AdminConnectedEvent(
                    connectionId = command.connectionId,
                    adminEmail = command.adminEmail
            )
    )
  }

  @EventSourcingHandler
  fun on(event: AdminConnectedEvent) {
    // handle event
    connectionId = event.connectionId
  }
}
