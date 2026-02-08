package boond.customeraccountlist

import com.fasterxml.jackson.annotation.JsonProperty
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
  @Column(name = "companyName") var companyName: String? = null
}

data class CustomerAccountListReadModel(val data: List<CustomerAccountListReadModelEntity>)

data class CustomerAccountLookupQuery(val customerId: UUID)

data class CustomerAccountLookupReadModel(
        val customerId: UUID,
        val companyId: Long,
        @JsonProperty("companyName") val companyName: String
)
