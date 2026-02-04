package boond.adminconnected

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

data class AdminConnectedReadModelQuery(val connectionId: UUID)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238263
*/
@Entity
class AdminConnectedReadModelEntity {
  @Id @Column(name = "connectionId") var connectionId: UUID? = null

  @Column(name = "adminEmail") var adminEmail: String? = null
}

data class AdminConnectedReadModel(val data: AdminConnectedReadModelEntity)
