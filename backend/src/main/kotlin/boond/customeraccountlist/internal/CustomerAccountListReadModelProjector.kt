package boond.customeraccountlist.internal

import boond.customeraccountlist.CustomerAccountListReadModelEntity
import boond.events.AccountCreatedEvent
import java.util.UUID
import mu.KotlinLogging
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface CustomerAccountListReadModelRepository :
    JpaRepository<CustomerAccountListReadModelEntity, UUID> {

  // The Guard: Check if the customer exists AND is tied to this specific company
  fun existsByCustomerIdAndCompanyId(customerId: UUID, companyId: Long): Boolean

  fun findByCustomerId(customerId: UUID): CustomerAccountListReadModelEntity?
}

@Component
class CustomerAccountListReadModelProjector(
    private val repository: CustomerAccountListReadModelRepository
) {

  @EventHandler
  fun on(event: AccountCreatedEvent) {
    // Convert String from event to UUID to match your Entity @Id
    val eventCustomerId = event.customerId

    // Find existing or new. Since customerId is @Id, findById is perfect.
    val entity =
        repository.findById(eventCustomerId).orElseGet { CustomerAccountListReadModelEntity() }

    entity
        .apply {
          this.customerId = eventCustomerId
          this.clientEmail = event.clientEmail
          this.companyId = event.companyId // Injected as Long
        }
        .also {
          logger.info {
            "Projecting AccountCreatedEvent for customer: ${event.customerId} (Company: ${event.companyId})"
          }
          repository.save(it)
        }
  }

  @ResetHandler
  fun reset() {
    logger.info { "Resetting CustomerAccountList Projection... Clearing database." }
    repository.deleteAllInBatch()
  }

  companion object {
    private val logger = KotlinLogging.logger {}
  }
}
