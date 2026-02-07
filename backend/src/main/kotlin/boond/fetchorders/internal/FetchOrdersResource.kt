package boond.fetchorders.internal

import boond.common.OrderInfo
import boond.domain.commands.fetchorders.MarkOrdersFetchedCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class FetchOrdersPayload(
    val sessionId: UUID,
    val companyId: Long,
    val customerId: UUID,
    val orders: List<OrderPayload>
)

data class OrderPayload(
    val orderId: Long,
    val companyId: Long,
    val projectId: Long,
    val reference: String,
    val title: String,
    val state: Int,
    val orderDate: String,
    val startDate: String,
    val endDate: String,
    val totalExcludingTaxes: Double,
    val totalIncludingTaxes: Double,
    val totalVat: Double
)

@RestController
class FetchOrdersResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/fetchorders/{id}")
  fun processCommand(
      @PathVariable("id") sessionId: UUID,
      @RequestBody payload: FetchOrdersPayload
  ): CompletableFuture<Any> {
    val orderInfos =
        payload.orders.map { order ->
          OrderInfo(
              orderId = order.orderId,
              companyId = order.companyId,
              projectId = order.projectId,
              reference = order.reference,
              title = order.title,
              state = order.state,
              orderDate = order.orderDate,
              startDate = order.startDate,
              endDate = order.endDate,
              // Directly mapping flattened fields
              totalExcludingTaxes = order.totalExcludingTaxes,
              totalIncludingTaxes = order.totalIncludingTaxes,
              totalVat = order.totalVat)
        }

    return commandGateway.send(
        MarkOrdersFetchedCommand(
            sessionId = payload.sessionId,
            companyId = payload.companyId,
            customerId = payload.customerId,
            orderList = orderInfos))
  }
}
