package boond.customeraccountlist.internal

import boond.customeraccountlist.CustomerAccountListReadModelEntity
import boond.events.AccountCreatedEvent
import java.util.UUID
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface CustomerAccountListReadModelRepository :
    JpaRepository<CustomerAccountListReadModelEntity, UUID>

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238324
*/
@Component
class CustomerAccountListReadModelProjector(
    var repository: CustomerAccountListReadModelRepository
) {

  @EventHandler
  fun on(event: AccountCreatedEvent) {
    // throws exception if not available (adjust logic)
    val entity =
        this.repository.findById(event.customerId).orElse(CustomerAccountListReadModelEntity())
    entity
        .apply {
          customerId = event.customerId
          clientEmail = event.clientEmail
          companyId = event.companyId
        }
        .also { this.repository.save(it) }
  }
}
