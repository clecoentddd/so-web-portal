package boond.events

import boond.common.Event
import boond.common.OrderInfo
import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238372
*/
data class OrdersFetchedEvent(
    val sessionId: UUID,
    val companyId: Long,
    val customerId: UUID,
    val orderList: List<OrderInfo>
) : Event
