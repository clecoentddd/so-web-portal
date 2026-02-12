package boond.fetchorders.internal

import boond.common.Processor
import boond.domain.commands.fetchorders.MarkOrdersFetchedCommand
import boond.events.ListOfProjectsFetchedEvent
import boond.fetchorder.internal.adapter.FetchBoondAPIOrderList
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238373
*/
@Component
class FetchOrderAutomationProcessor(
        private val commandGateway: CommandGateway,
        private val boondAdapter: FetchBoondAPIOrderList
) : Processor {

  private val logger = KotlinLogging.logger {}

  @EventHandler
  fun on(event: ListOfProjectsFetchedEvent) {
    logger.info {
      "Project list fetched for company ${event.companyId}. Fetching all orders from Boond..."
    }

    // 1. Fetch the data from the adapter (company-wide, no projectId)
    val ordersInfo = boondAdapter.fetch(companyId = event.companyId)

    logger.info { "Fetched ${ordersInfo.orders.size} orders for company ${event.companyId}" }

    // 2. Dispatch the command with the full list
    commandGateway.send<Any>(
                    MarkOrdersFetchedCommand(
                            sessionId = event.sessionId,
                            companyId = event.companyId,
                            customerId = event.customerId,
                            orderList = ordersInfo.orders
                    )
            )
            .exceptionally { throwable ->
              logger.error(throwable) {
                "FAILED to mark orders fetched for session ${event.sessionId}. " +
                        "Reason: ${throwable.message}"
              }
              null
            }

    logger.info {
      "Successfully dispatched MarkOrdersFetchedCommand for session ${event.sessionId} (${ordersInfo.orders.size} orders)"
    }
  }
}
