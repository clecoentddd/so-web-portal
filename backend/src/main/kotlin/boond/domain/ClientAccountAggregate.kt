package boond.domain

import boond.domain.commands.createclientaccount.CreateAccountCommand
import boond.events.AccountCreatedEvent
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

  @AggregateIdentifier var customerId: java.util.UUID? = null

  private var validCompanies: List<boond.common.CompanyInfo> = emptyList()

  @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
  @CommandHandler
  fun handle(command: CreateAccountCommand) {
    // These rules act as "Guards"
    requireNotNull(command.customerId) { "CustomerID must not be null" }
    requireNotNull(command.connectionId) { "ConnectionID must not be null" }
    require(command.clientEmail.contains("@")) { "Invalid email format" }
    require(command.companyId > 0) { "Company ID must be a positive number" }

    if (validCompanies.isNotEmpty() && validCompanies.none { it.companyId == command.companyId }) {
      throw boond.common.CommandException("Company ID ${command.companyId} does not exist")
    }

    AggregateLifecycle.apply(
        AccountCreatedEvent(
            customerId = command.customerId, // Use !! now that we've checked
            connectionId = command.connectionId,
            clientEmail = command.clientEmail,
            companyId = command.companyId))
  }

  @EventSourcingHandler
  fun on(event: AccountCreatedEvent) {
    // handle event
    customerId = event.customerId
  }

  @EventSourcingHandler
  fun on(event: boond.events.ListOfCompaniesFetchedEvent) {
    validCompanies = event.listOfCompanies
  }
}
