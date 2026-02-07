package boond.companyorderlist

import boond.common.OrderInfo
import jakarta.persistence.*
import java.util.UUID

// --- Queries ---
data class CompanyOrderListReadModelQuery(val sessionId: UUID)

data class ProjectOrdersQuery(val sessionId: UUID, val projectId: Long, val companyId: Long)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238377
*/
@Entity
@Table(name = "company_order_list_projections")
class CompanyOrderListReadModelEntity {
  @Id @Column(name = "session_id") var sessionId: UUID? = null

  @Column(name = "company_id") var companyId: Long? = null

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
          name = "companyorderlist_order_items",
          joinColumns = [JoinColumn(name = "session_id")]
  )
  var orderList: MutableList<OrderInfo> = mutableListOf()
}

// --- Wrapper for the Query API ---
class CompanyOrderListReadModel(entity: CompanyOrderListReadModelEntity) {
  var sessionId: UUID? = entity.sessionId
  var companyId: Long? = entity.companyId
  var orderList: MutableList<OrderInfo> = entity.orderList.toMutableList()
}
