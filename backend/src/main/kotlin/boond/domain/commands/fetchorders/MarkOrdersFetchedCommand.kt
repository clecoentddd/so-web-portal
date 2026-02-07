package boond.domain.commands.fetchorders

import boond.common.Command
import boond.common.OrderInfo
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238373
*/
data class MarkOrdersFetchedCommand(
    @TargetAggregateIdentifier val sessionId: UUID,
    val companyId: Long,
    val customerId: UUID,
    val orderList: List<OrderInfo>
) : Command
