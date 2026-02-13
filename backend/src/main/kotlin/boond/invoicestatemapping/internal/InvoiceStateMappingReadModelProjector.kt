package boond.invoicestatemapping.internal

import boond.events.InvoiceStateMappingUpdatedEvent
import boond.invoicestatemapping.InvoiceStateMappingProjectionEntity
import jakarta.transaction.Transactional
import java.time.Instant
import java.util.UUID
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

// --------------------
// Repository
// --------------------
interface InvoiceStateMappingProjectionRepository :
    JpaRepository<InvoiceStateMappingProjectionEntity, UUID> {
  @Transactional fun deleteBySettingsId(settingsId: UUID)

  fun findBySettingsId(settingsId: UUID): List<InvoiceStateMappingProjectionEntity>
}

// --------------------
// Tracking processor annotation
// --------------------
// All event handlers in this processing group will run as a tracking processor
@ProcessingGroup("invoiceStateMappingProjector")
@Component
class InvoiceStateMappingProjector(
    private val repository: InvoiceStateMappingProjectionRepository
) {

  @EventHandler
  @Transactional
  fun on(event: InvoiceStateMappingUpdatedEvent) {
    // 1. Delete all previous rows for this settingsId
    repository.deleteBySettingsId(event.settingsId)
    repository.flush() // force deletion before insert

    // 2. Insert fresh rows atomically
    val now: Long = Instant.now().toEpochMilli()

    val connectionId = event.connectionId as? UUID

    if (connectionId == null) {
      throw IllegalArgumentException("connectionId metadata missing")
    }

    val entities =
        event.invoiceStateMapping.map { mapping ->
          InvoiceStateMappingProjectionEntity(
              settingsId = event.settingsId,
              code = mapping.code,
              label = mapping.label,
              connectionId = connectionId,
              timestamp = now)
        }
    repository.saveAll(entities)
  }
}
