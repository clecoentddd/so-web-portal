package boond.domain

import boond.domain.commands.fetchinvoices.MarkInvoicesFetchedCommand
import boond.domain.commands.fetchorders.MarkOrdersFetchedCommand
import boond.domain.commands.fetchprojectslistfromboond.MarkListOfProjectsFetchedCommand
import boond.domain.commands.initiatecustomersession.InitiateSessionCommand
import boond.domain.commands.requestprojectdetails.RequestProjectDetailsCommand
import boond.events.CustomerSessionInitiatedEvent
import boond.events.InvoicesFetchedEvent
import boond.events.ListOfProjectsFetchedEvent
import boond.events.OrdersFetchedEvent
import boond.events.ProjectDetailsRequestedEvent
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
  private var companyId: Long? = null
  private var customerId: UUID? = null

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
    this.companyId = event.companyId
    this.customerId = event.customerId
    this.isActive = true
  }

  // List of projects
  @CommandHandler
  fun handle(command: MarkListOfProjectsFetchedCommand) {
    // Optional: Validation
    check(command.companyId == this.companyId) { "companyId mismatch" }
    check(command.customerId == this.customerId) { "customerId mismatch" }

    AggregateLifecycle.apply(
        ListOfProjectsFetchedEvent(
            sessionId = command.sessionId,
            companyId = command.companyId,
            customerId = command.customerId,
            projectList = command.projectList))
  }

  @EventSourcingHandler
  fun on(event: ListOfProjectsFetchedEvent) {
    // handle event
    sessionId = event.sessionId
  }

  // Specific project details
  @CommandHandler
  fun handle(command: RequestProjectDetailsCommand) {
    check(command.companyId == this.companyId) { "companyId mismatch" }
    check(command.customerId == this.customerId) { "customerId mismatch" }

    AggregateLifecycle.apply(
        ProjectDetailsRequestedEvent(
            sessionId = command.sessionId,
            companyId = command.companyId,
            customerId = command.customerId,
            projectId = command.projectId))
  }

  @EventSourcingHandler
  fun on(event: ProjectDetailsRequestedEvent) {
    // handle event
    sessionId = event.sessionId
  }

  // List of orders
  @CommandHandler
  fun handle(command: MarkOrdersFetchedCommand) {
    check(command.companyId == this.companyId) { "companyId mismatch" }
    check(command.customerId == this.customerId) { "customerId mismatch" }

    AggregateLifecycle.apply(
        OrdersFetchedEvent(
            sessionId = command.sessionId,
            companyId = command.companyId,
            customerId = command.customerId,
            orderList = command.orderList))
  }

  @EventSourcingHandler
  fun on(event: OrdersFetchedEvent) {
    // handle event
    sessionId = event.sessionId
  }

  // List of invoices
  @CommandHandler
  fun handle(command: MarkInvoicesFetchedCommand) {
    check(command.companyId == this.companyId) { "companyId mismatch" }
    check(command.customerId == this.customerId) { "customerId mismatch" }

    AggregateLifecycle.apply(
        InvoicesFetchedEvent(
            sessionId = command.sessionId,
            companyId = command.companyId,
            customerId = command.customerId,
            invoiceList = command.invoiceList))
  }

  @EventSourcingHandler
  fun on(event: InvoicesFetchedEvent) {
    // handle event
    sessionId = event.sessionId
  }
}
