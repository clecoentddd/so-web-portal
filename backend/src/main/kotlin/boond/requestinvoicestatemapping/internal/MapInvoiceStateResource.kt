package boond.requestinvoicestatemapping.internal

import boond.common.SettingsConstants
import boond.domain.commands.requestinvoicestatemapping.MapInvoiceStateCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class RequestInvoiceStateMappingPayload(val connectionId: UUID)

@RestController
@RequestMapping("/requestinvoicestatemapping")
class MapInvoiceStateResource(private val commandGateway: CommandGateway) {

  var logger = KotlinLogging.logger {}

  @PostMapping
  fun processCommand(
      @RequestBody payload: RequestInvoiceStateMappingPayload
  ): CompletableFuture<Any> {
    // Simply send MapInvoiceStateCommand to existing singleton aggregate
    logger.info("Received request to map invoice state for connection: ${payload.connectionId}")
    return commandGateway.send(
        MapInvoiceStateCommand(
            settingsId = SettingsConstants.SETTINGS_ID, connectionId = payload.connectionId))
  }
}
