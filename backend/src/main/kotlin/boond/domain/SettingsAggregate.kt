package boond.domain

import boond.common.InvoiceStateMappingInfo
import boond.domain.commands.fetchinvoicestatemapping.MarkInvoiceStateMappingFetchedCommand
import boond.domain.commands.initializesettings.InitializeSettingsCommand
import boond.domain.commands.requestcompanylistupdate.RequestCompanyListUpdateCommand
import boond.domain.commands.requestinvoicestatemapping.MapInvoiceStateCommand
import boond.domain.commands.requestinvoicestatemappingupdate.UpdateInvoiceStateMappingCommand
import boond.events.*
import boond.events.CompanyListUpdateRequestedEvent
import java.util.UUID
import mu.KotlinLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class SettingsAggregate private constructor() {

  private val logger = KotlinLogging.logger {}

  @AggregateIdentifier lateinit var settingsId: UUID

  var invoiceStateMapping: List<InvoiceStateMappingInfo> = emptyList()
  var companyList: List<boond.common.CompanyInfo> = emptyList()
  var state: String = "INCOMPLETE"

  // ----------------------
  // Constructor Command — Initialize aggregate
  // ----------------------
  @CommandHandler
  constructor(cmd: InitializeSettingsCommand) : this() {
    settingsId = cmd.settingsId
    logger.info(
        "Initializing SettingsAggregate with ID $settingsId, connectionId: ${cmd.connectionId}")

    // Apply event — metadata (connectionId) is automatically propagated by
    // CorrelationDataInterceptor
    AggregateLifecycle.apply(
        SettingsInitializedEvent(settingsId = settingsId, connectionId = cmd.connectionId))
  }

  // ----------------------
  // Other Command Handlers
  // ----------------------
  @CommandHandler
  fun handle(command: MapInvoiceStateCommand) {
    logger.info("MapInvoiceStateCommand received for SettingsAggregate $settingsId")

    // Metadata automatically propagated
    AggregateLifecycle.apply(
        InvoiceStateMappingRequestedEvent(
            settingsId = command.settingsId, connectionId = command.connectionId))
  }

  @CommandHandler
  fun handle(command: MarkInvoiceStateMappingFetchedCommand) {
    if (command.invoiceStateMapping.isNotEmpty()) {
      logger.info(
          "MarkInvoiceStateMappingFetchedCommand received for SettingsAggregate $settingsId, ${command.invoiceStateMapping.size} items")

      // Metadata automatically propagated
      AggregateLifecycle.apply(
          InvoiceStateMappingUpdatedEvent(
              settingsId = command.settingsId,
              connectionId = command.connectionId,
              invoiceStateMapping = command.invoiceStateMapping))
    }
  }

  @CommandHandler
  fun handle(command: UpdateInvoiceStateMappingCommand) {
    logger.info("UpdateInvoiceStateMappingCommand received for SettingsAggregate $settingsId")

    // Metadata automatically propagated
    AggregateLifecycle.apply(
        InvoiceStateMappingUpdateRequestedEvent(
            settingsId = command.settingsId, connectionId = command.connectionId))
  }

  @CommandHandler
  fun handle(command: RequestCompanyListUpdateCommand) {

    AggregateLifecycle.apply(
        CompanyListUpdateRequestedEvent(
            settingsId = command.settingsId, connectionId = command.connectionId))
  }

  @CommandHandler
  fun handle(
      command: boond.domain.commands.fetchcompanieslistfromboond.MarkListOfCompaniesFetchedCommand
  ) {
    logger.info(
        "MarkListOfCompaniesFetchedCommand received for SettingsAggregate $settingsId, ${command.listOfCompanies.size} companies")

    // Metadata automatically propagated
    AggregateLifecycle.apply(
        boond.events.ListOfCompaniesFetchedEvent(
            settingsId = command.settingsId,
            connectionId = command.connectionId,
            listOfCompanies = command.listOfCompanies))
  }

  @CommandHandler
  fun handle(
      command: boond.domain.commands.fetchcompanieslistfromboond.RequestCompanyListUpdateCommand
  ) {
    logger.info("RequestCompanyListUpdateCommand received for SettingsAggregate $settingsId")
    AggregateLifecycle.apply(
        CompanyListUpdateRequestedEvent(
            settingsId = settingsId, connectionId = command.connectionId))
  }

  // ----------------------
  // Event Sourcing Handlers (Domain State Only)
  // ----------------------
  @EventSourcingHandler
  fun on(event: SettingsInitializedEvent) {
    settingsId = event.settingsId
    state = "INCOMPLETE"
    invoiceStateMapping = emptyList()
    logger.info("SettingsInitializedEvent applied for SettingsAggregate $settingsId")
  }

  @EventSourcingHandler
  fun on(event: InvoiceStateMappingRequestedEvent) {
    state = "INCOMPLETE"
    logger.info("InvoiceStateMappingRequestedEvent applied for SettingsAggregate $settingsId")
  }

  @EventSourcingHandler
  fun on(event: InvoiceStateMappingUpdatedEvent) {
    invoiceStateMapping = event.invoiceStateMapping
    if (invoiceStateMapping.isNotEmpty()) state = "COMPLETE"
    logger.info(
        "InvoiceStateMappingUpdatedEvent applied for SettingsAggregate $settingsId, total items: ${invoiceStateMapping.size}")
  }

  @EventSourcingHandler
  fun on(event: InvoiceStateMappingUpdateRequestedEvent) {
    state = "INCOMPLETE"
    logger.info("InvoiceStateMappingUpdateRequestedEvent applied for SettingsAggregate $settingsId")
  }

  @EventSourcingHandler
  fun on(event: boond.events.ListOfCompaniesFetchedEvent) {
    companyList = event.listOfCompanies
    logger.info(
        "ListOfCompaniesFetchedEvent applied for SettingsAggregate $settingsId, total companies: ${companyList.size}")
  }

  @EventSourcingHandler
  fun on(event: CompanyListUpdateRequestedEvent) {
    state = "INCOMPLETE"
    logger.info("CompanyListUpdateRequestedEvent applied for SettingsAggregate $settingsId")
  }
}
