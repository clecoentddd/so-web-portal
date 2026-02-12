package boond.fetchinvoicestatemapping.internal

import boond.common.InvoiceStateMappingInfo
import boond.common.Processor
import boond.domain.commands.fetchinvoicestatemapping.MarkInvoiceStateMappingFetchedCommand
import boond.events.InvoiceStateMappingUpdateRequestedEvent
import boond.events.SettingsInitializedEvent
import boond.fetchinvoicestatemapping.internal.adapter.FetchBoondAPIInvoiceStateMapping
import java.util.UUID
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class ProcessorFetchInvoiceStateMappingProcessor(
        private val commandGateway: CommandGateway,
        private val boondAdapter: FetchBoondAPIInvoiceStateMapping
) : Processor {

  private val logger = KotlinLogging.logger {}

  // ---------- SETTINGS INITIALIZED ----------
  @EventHandler
  fun on(event: SettingsInitializedEvent) {
    logger.info { "SettingsInitializedEvent received for connection ${event.connectionId}" }
    fetchAndDispatch(event.settingsId, event.connectionId)
  }

  // ---------- UPDATE REQUESTED ----------
  @EventHandler
  fun on(event: InvoiceStateMappingUpdateRequestedEvent) {
    logger.info {
      "InvoiceStateMappingUpdateRequestedEvent received for connection ${event.connectionId}"
    }
    fetchAndDispatch(event.settingsId, event.connectionId)
  }

  // ---------- SHARED FLOW ----------
  private fun fetchAndDispatch(settingsId: UUID, connectionId: UUID) {

    logger.info { "Invoice state mapping requested for connection $connectionId..." }

    val stateMappingInfo = boondAdapter.fetchAll()

    val statesList =
            stateMappingInfo.states.map { state ->
              InvoiceStateMappingInfo(code = state.code, label = state.label)
            }

    commandGateway.send<Any>(
                    MarkInvoiceStateMappingFetchedCommand(
                            settingsId = settingsId,
                            connectionId = connectionId,
                            invoiceStateMapping = statesList
                    )
            )
            .exceptionally { throwable ->
              logger.error(throwable) {
                "FAILED to mark invoice state mapping for connection $connectionId"
              }
              null
            }
  }
}
