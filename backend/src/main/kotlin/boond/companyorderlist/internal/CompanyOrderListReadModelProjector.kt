package boond.companyorderlist.internal

import boond.companyorderlist.CompanyOrderListReadModelEntity
import boond.events.OrdersFetchedEvent
import java.util.UUID
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface CompanyOrderListReadModelRepository :
    JpaRepository<CompanyOrderListReadModelEntity, UUID>

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238377
*/

@Component
class CompanyOrderListReadModelProjector(var repository: CompanyOrderListReadModelRepository) {

  @EventHandler
  fun on(event: OrdersFetchedEvent) {
    // throws exception if not available (adjust logic)
    val entity = this.repository.findById(event.sessionId).orElse(CompanyOrderListReadModelEntity())
    entity
        .apply {
          companyId = event.companyId
          orderList = event.orderList.toMutableList()
          sessionId = event.sessionId
        }
        .also { this.repository.save(it) }
  }
}
