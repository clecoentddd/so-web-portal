package boond.customersessions

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

data class CustomerSessionsReadModelQuery(val sessionId: UUID)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238306
*/

@Entity
class CustomerSessionsReadModelEntity {
  @Id @Column(name = "sessionId") var sessionId: UUID? = null
  @Column(name = "customerId") var customerId: UUID? = null
  @Column(name = "companyId") var companyId: Long? = null

  @Column(name = "lastUpdated") var lastUpdated: java.time.Instant = java.time.Instant.now()
}

data class CustomerSessionsReadModel(val data: CustomerSessionsReadModelEntity)
