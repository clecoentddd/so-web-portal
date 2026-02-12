package boond.companylistlookup.internal

import boond.companylistlookup.CompanyListLookUpReadModelEntity
import boond.events.ListOfCompaniesFetchedEvent
import java.time.Instant
import java.util.UUID
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/** REPOSITORY: Standard JPA repository using UUID as the ID. */
@Repository
interface CompanyListLookUpReadModelRepository :
        JpaRepository<CompanyListLookUpReadModelEntity, UUID> {
  @Transactional fun deleteBySettingsId(settingsId: UUID)

  fun findByCompanyId(companyId: Long): CompanyListLookUpReadModelEntity?
}

/**
 * PROJECTOR: Listens for the ListOfCompaniesFetchedEvent and synchronizes the persistent lookup
 * table.
 */
@Component
class CompanyListLookUpReadModelProjector(
        private val repository: CompanyListLookUpReadModelRepository
) {

  /**
   * When the event is received, we perform a "Full Update": 1. Clear the existing table to handle
   * deletions from the source. 2. Map the domain objects (CompanyInfo) to the Entity. 3. Save the
   * new batch.
   */
  @EventHandler
  @Transactional
  fun on(event: ListOfCompaniesFetchedEvent) {
    // 1. Clear the table for this 'Full Update' strategy
    val settingsId = event.settingsId
    if (settingsId != null) {
      repository.deleteBySettingsId(settingsId)
      repository.flush() // force deletion before insert
    }

    // 2. Insert fresh rows atomically
    val now: Long = Instant.now().toEpochMilli()

    val connectionId = event.connectionId

    if (connectionId == null || settingsId == null) {
      throw IllegalArgumentException("connectionId or settingsId metadata missing")
    }

    // 2. Map the list from the event to our persistent entities
    val entities =
            event.listOfCompanies.map { info ->
              CompanyListLookUpReadModelEntity().apply {
                this.companyId = info.companyId
                this.companyName = info.companyName
                this.settingsId = settingsId
                this.connectionId = connectionId
                this.timestamp = now
              }
            }

    // 3. Save all new records to the database
    if (entities.isNotEmpty()) {
      repository.saveAll(entities)
    }
  }
}
