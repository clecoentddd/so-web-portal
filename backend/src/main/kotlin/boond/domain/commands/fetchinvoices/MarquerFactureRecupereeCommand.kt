package boond.domain.commands.fetchinvoices

import org.axonframework.modelling.command.TargetAggregateIdentifier
import boond.common.Command
import java.util.UUID;
import kotlin.collections.List;


/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238313
*/
data class MarquerFactureRecupereeCommand(
    @TargetAggregateIdentifier var sessionId:UUID,
	var companyId:Long,
	var customerId:UUID,
	var invoiceList:List<String>
): Command
