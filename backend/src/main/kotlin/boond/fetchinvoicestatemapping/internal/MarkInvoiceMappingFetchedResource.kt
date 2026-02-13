package boond.fetchinvoicestatemapping.internal

import boond.common.InvoiceStateMappingInfo
import boond.common.SettingsConstants
import boond.domain.commands.fetchinvoicestatemapping.MarkInvoiceStateMappingFetchedCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.*

data class FetchInvoiceStateMappingPayload(
    val connectionId: UUID,
    val adminEmail: String,
    val listOfStates: List<InvoiceStateMappingInfo> = emptyList()
)

@RestController
class MarkInvoiceStateMappingFetchedResource(private val commandGateway: CommandGateway) {

  private val logger = KotlinLogging.logger {}

  /** DEBUG ROUTE */
  @CrossOrigin
  @PostMapping("/debug/fetchinvoicestatemapping")
  fun processDebugCommand(
      @RequestParam connectionId: UUID,
      @RequestBody listOfStates: List<InvoiceStateMappingInfo>
  ): CompletableFuture<Any> {
    return commandGateway.send<Any>(
        MarkInvoiceStateMappingFetchedCommand(
            settingsId = SettingsConstants.SETTINGS_ID,
            connectionId = connectionId,
            invoiceStateMapping = listOfStates))
  }

  /** MAIN ROUTE */
  @CrossOrigin
  @PostMapping("/fetchinvoicestatemapping")
  fun processCommand(
      @RequestBody payload: FetchInvoiceStateMappingPayload
  ): CompletableFuture<Any> {

    logger.info {
      "Manually marking invoice state mapping fetched for connection ${payload.connectionId}"
    }

    return commandGateway.send(
        MarkInvoiceStateMappingFetchedCommand(
            settingsId = SettingsConstants.SETTINGS_ID, // Reuse existing ID
            connectionId = payload.connectionId,
            invoiceStateMapping = payload.listOfStates))
  }
}
