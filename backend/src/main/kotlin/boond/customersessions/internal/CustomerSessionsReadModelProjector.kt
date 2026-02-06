package boond.customersessions.internal

import boond.customersessions.CustomerSessionsReadModelEntity
import boond.events.CustomerSessionInitiatedEvent
import java.time.Instant
import java.util.Optional
import java.util.UUID
import mu.KotlinLogging
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface CustomerSessionsReadModelRepository :
    JpaRepository<CustomerSessionsReadModelEntity, UUID> {

  fun findTopByCustomerIdAndCompanyIdOrderByLastUpdatedDesc(
      customerId: UUID,
      companyId: Long
  ): Optional<CustomerSessionsReadModelEntity>

  fun findByCustomerIdAndCompanyId(
      customerId: UUID,
      companyId: Long
  ): Optional<CustomerSessionsReadModelEntity>
}

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238324
*/
@Component
class CustomerSessionsReadModelProjector(
    private val repository: CustomerSessionsReadModelRepository
) {

  @EventHandler
  fun on(event: CustomerSessionInitiatedEvent) {
    // 1. Clean up the old session for this specific customer/company pair
    repository.findByCustomerIdAndCompanyId(event.customerId, event.companyId).ifPresent {
        oldSession ->
      logger.info {
        "Cleaning up old session ${oldSession.sessionId} for customer ${event.customerId}"
      }
      repository.delete(oldSession)
      // Flush ensures the delete hits the DB before the new save
      repository.flush()
    }

    // 2. Create the new session with a fresh timestamp
    val entity =
        CustomerSessionsReadModelEntity().apply {
          this.sessionId = event.sessionId
          this.companyId = event.companyId
          this.customerId = event.customerId
          this.lastUpdated = Instant.now()
        }

    repository.save(entity)
    logger.info { "Projected new session ${event.sessionId} for customer ${event.customerId}" }
  }

  @ResetHandler
  fun reset() {
    logger.info { "Resetting CustomerSessions Projection... Clearing database." }
    repository.deleteAllInBatch()
  }

  companion object {
    private val logger = KotlinLogging.logger {}
  }
}
