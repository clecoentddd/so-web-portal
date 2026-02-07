package boond.fetchinvoices.internal

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import boond.domain.commands.fetchinvoices.MarquerFactureRecupereeCommand

import java.util.UUID;
import kotlin.collections.List;

import java.util.concurrent.CompletableFuture


data class FetchinvoicesPayload(	var sessionId:UUID,
	var companyId:Long,
	var customerId:UUID,
	var invoiceList:List<String>)

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238313
*/
@RestController
class MarquerFactureRecupereeResource(private var commandGateway: CommandGateway) {

    var logger = KotlinLogging.logger {}

    
    @CrossOrigin
    @PostMapping("/debug/fetchinvoices")
    fun processDebugCommand(@RequestParam sessionId:UUID,
	@RequestParam companyId:Long,
	@RequestParam customerId:UUID,
	@RequestParam invoiceList:List<String>):CompletableFuture<Any> {
        return commandGateway.send(MarquerFactureRecupereeCommand(sessionId,
	companyId,
	customerId,
	invoiceList))
    }
    

    
       @CrossOrigin
       @PostMapping("/fetchinvoices/{id}")
    fun processCommand(
        @PathVariable("id") sessionId: UUID,
        @RequestBody payload: FetchinvoicesPayload
    ):CompletableFuture<Any> {
         return commandGateway.send(MarquerFactureRecupereeCommand(			sessionId=payload.sessionId,
			companyId=payload.companyId,
			customerId=payload.customerId,
			invoiceList=payload.invoiceList))
        }
       

}
