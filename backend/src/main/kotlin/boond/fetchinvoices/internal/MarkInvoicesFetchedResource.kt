package boond.fetchinvoices.internal

import boond.common.InvoiceInfo
import boond.domain.commands.fetchinvoices.MarkInvoicesFetchedCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class FetchInvoicesPayload(
    val sessionId: UUID,
    val companyId: Long,
    val customerId: UUID,
    val invoices: List<InvoicePayload>
)

data class InvoicePayload(
    val invoiceId: Long,
    val companyId: Long,
    val projectId: Long,
    val orderId: Long,
    val reference: String,
    val title: String,
    val state: Int,
    val invoiceDate: String,
    val dueDate: String,
    val performedDate: String,
    val totalExcludingTaxes: Double,
    val totalIncludingTaxes: Double,
    val totalVat: Double
)

@RestController
class FetchInvoicesResource(private var commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/fetchinvoices/{id}")
  fun processCommand(
      @PathVariable("id") sessionId: UUID,
      @RequestBody payload: FetchInvoicesPayload
  ): CompletableFuture<Any> {
    val invoiceInfos =
        payload.invoices.map { invoice ->
          InvoiceInfo(
              invoiceId = invoice.invoiceId,
              companyId = invoice.companyId,
              projectId = invoice.projectId,
              orderId = invoice.orderId,
              reference = invoice.reference,
              title = invoice.title,
              state = invoice.state,
              invoiceDate = invoice.invoiceDate,
              dueDate = invoice.dueDate,
              performedDate = invoice.performedDate,
              // Directly mapping flattened fields
              totalExcludingTaxes = invoice.totalExcludingTaxes,
              totalIncludingTaxes = invoice.totalIncludingTaxes,
              totalVat = invoice.totalVat)
        }

    return commandGateway.send(
        MarkInvoicesFetchedCommand(
            sessionId = payload.sessionId,
            companyId = payload.companyId,
            customerId = payload.customerId,
            invoiceList = invoiceInfos))
  }
}
