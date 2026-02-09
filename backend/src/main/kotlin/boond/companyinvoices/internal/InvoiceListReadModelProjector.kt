package boond.companyinvoices.internal

import boond.companyinvoices.InvoiceListReadModelEntity
import boond.events.InvoicesFetchedEvent
import java.util.UUID
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface InvoiceListReadModelRepository : JpaRepository<InvoiceListReadModelEntity, UUID>

@Component
class InvoiceListReadModelProjector(private val repository: InvoiceListReadModelRepository) {

  @EventHandler
  fun on(event: InvoicesFetchedEvent) {
    // Find existing session projection or create a fresh one
    val entity = repository.findById(event.sessionId).orElse(InvoiceListReadModelEntity())

    entity.apply {
      sessionId = event.sessionId
      companyId = event.companyId
      // Clear and replace to avoid JPA collection mapping issues
      invoiceList.clear()
      invoiceList.addAll(event.invoiceList)
    }

    repository.save(entity)
  }
}
