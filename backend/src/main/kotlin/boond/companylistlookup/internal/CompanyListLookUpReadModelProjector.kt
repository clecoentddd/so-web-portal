package boond.companylistlookup.internal

import boond.companylistlookup.CompanyListLookUpReadModelEntity
import boond.events.ListOfCompaniesFetchedEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/** REPOSITORY: Standard JPA repository using Long (companyId) as the ID. */
@Repository
interface CompanyListLookUpReadModelRepository :
    JpaRepository<CompanyListLookUpReadModelEntity, Long>

/**
 * PROJECTOR: Listens for the ListOfCompaniesFetchedEvent and synchronizes the persistent lookup
 * table.
 */
@Component
class CompanyListLookUpReadModelProjector(
    private val repository: CompanyListLookUpReadModelRepository
) {

  /**
   * When the event is received, we perform a "Full Update":
   * 1. Clear the existing table to handle deletions from the source.
   * 2. Map the domain objects (CompanyInfo) to the Entity.
   * 3. Save the new batch.
   */
  @EventHandler
  @Transactional
  fun on(event: ListOfCompaniesFetchedEvent) {
    // 1. Clear the table for this 'Full Update' strategy
    // deleteAllInBatch is more efficient than deleteAll() for large lists
    repository.deleteAllInBatch()

    // 2. Map the list from the event to our persistent entities
    val entities =
        event.listOfCompanies.map { info ->
          CompanyListLookUpReadModelEntity().apply {
            this.companyId = info.companyId
            this.companyName = info.companyName
          }
        }

    // 3. Save all new records to the database
    if (entities.isNotEmpty()) {
      repository.saveAll(entities)
    }
  }
}
