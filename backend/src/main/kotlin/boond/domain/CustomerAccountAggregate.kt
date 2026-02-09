package boond.domain

import boond.common.CommandException
import boond.domain.commands.createcustomeraccount.CreateAccountCommand
import boond.domain.commands.customeraccountconnection.ToConnectToAccountCommand
import boond.events.AccountCreatedEvent
import boond.events.CustomerConnectedEvent
import java.util.UUID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ClientAccountAggregate {

  @AggregateIdentifier var customerId: UUID? = null

  // Internal state to track ownership and company context
  private var registeredEmail: String? = null
  private var companyId: Long = 0
  private var companyName: String = ""

  constructor()

  @CommandHandler
  @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
  fun handle(command: CreateAccountCommand) {
    requireNotNull(command.customerId) { "CustomerID must not be null" }
    requireNotNull(command.connectionId) { "ConnectionID must not be null" }
    require(command.clientEmail.contains("@")) { "Invalid email format" }
    require(command.companyId > 0) { "Company ID must be a positive number" }

    AggregateLifecycle.apply(
        AccountCreatedEvent(
            customerId = command.customerId,
            connectionId = command.connectionId,
            clientEmail = command.clientEmail,
            companyId = command.companyId,
            companyName = command.companyName))
  }

  @CommandHandler
  fun handle(command: ToConnectToAccountCommand) {
    require(command.clientEmail.contains("@")) { "Invalid email format" }

    // Ownership Guard
    if (this.registeredEmail != command.clientEmail) {
      throw CommandException("Access Denied: Email ${command.clientEmail} does not match owner.")
    }

    // Use 'this.companyId' (from Aggregate state), NOT 'command.companyId'
    AggregateLifecycle.apply(
        CustomerConnectedEvent(
            customerId = command.customerId,
            clientEmail = command.clientEmail,
            companyId = this.companyId))
  }

  // --- STATE RECONSTRUCTION ---

  @EventSourcingHandler
  fun on(event: AccountCreatedEvent) {
    this.customerId = event.customerId
    this.registeredEmail = event.clientEmail
    this.companyId = event.companyId // Store the ID for future use
    this.companyName = event.companyName
  }

  @EventSourcingHandler
  fun on(event: CustomerConnectedEvent) {
    this.customerId = event.customerId
    // Optional: you could update this.companyId here if it ever changes,
    // but usually it stays the same as set in AccountCreated.
  }
}
