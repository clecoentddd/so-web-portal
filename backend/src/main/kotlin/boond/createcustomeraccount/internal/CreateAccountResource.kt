package boond.createcustomeraccount.internal

import boond.domain.commands.createcustomeraccount.CreateAccountCommand
import java.util.UUID
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.MetaData
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

// 1. Keep the Payload right here in the same file
data class CreateCustomerAccountPayload(
    val connectionId: UUID?,
    val clientEmail: String?,
    val companyId: Long?,
    val companyName: String?
)

@RestController
class CreateAccountResource(private val commandGateway: CommandGateway) {

  private val logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/createcustomeraccount")
  fun processCommand(@RequestBody payload: CreateCustomerAccountPayload): Map<String, String> {
    logger.info { "CreateCustomerAccount Payload received: $payload" }

    // 2. Simple validation - if it fails, it throws an error automatically
    val cid = payload.connectionId ?: throw IllegalArgumentException("connectionId is missing")
    val email = payload.clientEmail ?: throw IllegalArgumentException("clientEmail is missing")
    val compId = payload.companyId ?: throw IllegalArgumentException("companyId is missing")
    val compName = payload.companyName ?: throw IllegalArgumentException("companyName is missing")

    val generatedId = UUID.randomUUID()

    // 3. Send it
    commandGateway.sendAndWait<Any>(
        CreateAccountCommand(
            customerId = generatedId,
            clientEmail = email,
            companyId = compId,
            companyName = compName,
            connectionId = cid),
        MetaData.with("COMPANY_ID", compId))

    return mapOf("customerId" to generatedId.toString())
  }

  // 4. Simple error catcher to prevent 500 errors in Swagger
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException::class)
  fun handleBadRequest(ex: IllegalArgumentException): Map<String, String?> {
    return mapOf("error" to ex.message)
  }
}
