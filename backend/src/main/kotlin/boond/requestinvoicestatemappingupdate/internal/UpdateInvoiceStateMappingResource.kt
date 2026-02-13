package boond.requestinvoicestatemappingupdate.internal

import boond.common.SettingsConstants
import boond.domain.commands.requestinvoicestatemappingupdate.UpdateInvoiceStateMappingCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateInvoiceStateMappingResource(private val commandGateway: CommandGateway) {

  private val logger = KotlinLogging.logger {}

  @CrossOrigin
  @PostMapping("/requestinvoicestatemappingupdate/{connectionId}")
  fun processCommand(@PathVariable connectionId: UUID): CompletableFuture<Any> {

    logger.info { "Request invoice state mapping update for $connectionId" }

    return commandGateway.send(
        UpdateInvoiceStateMappingCommand(SettingsConstants.SETTINGS_ID, connectionId))
  }
}
