package boond.adminconnected.internal

import boond.adminconnected.AdminConnectedReadModelEntity
import boond.events.AdminConnectedEvent
import java.util.UUID
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface AdminConnectedReadModelRepository : JpaRepository<AdminConnectedReadModelEntity, UUID>

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238263
*/

@ProcessingGroup("projects-boond-workflow-process")
@Component
class AdminConnectedReadModelProjector(var repository: AdminConnectedReadModelRepository) {

  @EventHandler
  fun on(event: AdminConnectedEvent) {
    // throws exception if not available (adjust logic)
    val entity =
        this.repository.findById(event.connectionId).orElse(AdminConnectedReadModelEntity())
    entity
        .apply {
          connectionId = event.connectionId
          adminEmail = event.adminEmail
        }
        .also { this.repository.save(it) }
  }
}
