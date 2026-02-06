package boond.companyprojectlist.internal

import boond.companyprojectlist.CompanyProjectListReadModelEntity
import boond.events.ListOfProjectsFetchedEvent
import java.util.UUID
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface CompanyProjectListReadModelRepository :
    JpaRepository<CompanyProjectListReadModelEntity, UUID>

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238316
*/
@Component
class CompanyProjectListReadModelProjector(var repository: CompanyProjectListReadModelRepository) {

  @EventHandler
  fun on(event: ListOfProjectsFetchedEvent) {
    // throws exception if not available (adjust logic)
    val entity =
        this.repository.findById(event.sessionId).orElse(CompanyProjectListReadModelEntity())
    entity
        .apply {
          sessionId = event.sessionId
          companyId = event.companyId
          customerId = event.customerId
          projectList = event.projectList.toMutableList()
        }
        .also { this.repository.save(it) }
  }
}
