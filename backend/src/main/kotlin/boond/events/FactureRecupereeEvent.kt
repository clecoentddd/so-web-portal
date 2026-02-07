package boond.events

import boond.common.Event

import java.util.UUID;
import kotlin.collections.List;


/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238233
*/
data class FactureRecupereeEvent(
    var sessionId:UUID,
	var companyId:Long,
	var customerId:UUID,
	var invoiceList:List<String>
) : Event
