package boond.fetchinvoices.internal

import boond.common.Processor
import boond.domain.commands.fetchinvoices.MarkInvoicesFetchedCommand
import boond.events.ListOfProjectsFetchedEvent
import boond.fetchorder.internal.adapter.FetchBoondAPIInvoiceList
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238373
*/
@Component
class FetchInvoiceAutomationProcessor(
    private val commandGateway: CommandGateway,
    private val boondAdapter: FetchBoondAPIInvoiceList
) : Processor {

  private val logger = KotlinLogging.logger {}

  @EventHandler
  fun on(event: ListOfProjectsFetchedEvent) {
    logger.info {
      "Project list fetched for company ${event.companyId}. Fetching all Invoices from Boond..."
    }

    // 1. Fetch the data from the adapter (company-wide, no projectId)
    val InvoicesInfo = boondAdapter.fetch(companyId = event.companyId)

    logger.info { "Fetched ${InvoicesInfo.invoices.size} Invoices for company ${event.companyId}" }

    // 2. Dispatch the command with the full list
    commandGateway
        .send<Any>(
            MarkInvoicesFetchedCommand(
                sessionId = event.sessionId,
                companyId = event.companyId,
                customerId = event.customerId,
                invoiceList = InvoicesInfo.invoices))
        .exceptionally { throwable ->
          logger.error(throwable) {
            "FAILED to mark Invoices fetched for session ${event.sessionId}. " +
                "Reason: ${throwable.message}"
          }
          null
        }

    logger.info {
      "Successfully dispatched MarkInvoicesFetchedCommand for session ${event.sessionId} (${InvoicesInfo.invoices.size} Invoices)"
    }
  }
}
