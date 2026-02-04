package boond.customeraccountlist

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

class CustomerAccountListReadModelQuery()

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238324
*/
@Entity
class CustomerAccountListReadModelEntity {
  @Id @Column(name = "customerId") var customerId: UUID? = null
  @Column(name = "clientEmail") var clientEmail: String? = null
  @Column(name = "companyId") var companyId: Long? = null
}

data class CustomerAccountListReadModel(val data: List<CustomerAccountListReadModelEntity>)
