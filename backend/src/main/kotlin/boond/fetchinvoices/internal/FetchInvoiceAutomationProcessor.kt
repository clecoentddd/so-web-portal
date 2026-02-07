package boond.fetchinvoices.internal

import boond.companyorderlist.CompanyOrderListReadModel
import boond.companyorderlist.CompanyOrderListReadModelQuery
import boond.common.Processor
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.axonframework.eventhandling.EventHandler
import boond.domain.commands.fetchinvoices.MarquerFactureRecupereeCommand
import java.util.UUID;
import kotlin.collections.List;

import boond.events.OrdersFetchedEvent

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238311
*/
@Component
class FetchInvoiceAutomationProcessor: Processor {
   var logger = KotlinLogging.logger {}

     @Autowired
     lateinit var commandGateway: CommandGateway
     @Autowired
     lateinit var queryGateway: QueryGateway


                @EventHandler
                fun on(event: OrdersFetchedEvent) {
                     queryGateway.query(
            CompanyOrderListReadModelQuery(event.aggregateId),
            CompanyOrderListReadModel::class.java
        ).thenAccept {
                /*commandGateway.send<MarquerFactureRecupereeCommand>(
                    MarquerFactureRecupereeCommand(
                      			sessionId=it.sessionId
			companyId=it.companyId
			customerId=it.customerId)
                )*/
        }
                }

}

