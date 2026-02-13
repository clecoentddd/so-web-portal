package boond.fetchcompanieslistfromboond.internal

import boond.common.CompanyInfo // Import the shared class
import boond.domain.commands.fetchcompanieslistfromboond.MarkListOfCompaniesFetchedCommand
import java.util.UUID
import java.util.concurrent.CompletableFuture
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.*

/** Payload adapted to use CompanyInfo and camelCase */
data class FetchCompaniesListFromBoondPayload(
    var settingsId: UUID,
    var connectionId: UUID,
    var listOfCompanies: List<CompanyInfo> = emptyList()
)

@RestController
class MarkListOfCompaniesFetchedResource(private var commandGateway: CommandGateway) {

  private val logger = KotlinLogging.logger {}

  /**
   * DEBUG ROUTE Note: Passing a List via @RequestParam is tricky in Swagger. Usually, it's better
   * to use a Body for lists.
   */
  @CrossOrigin
  @PostMapping("/debug/fetchcompanieslistfromboond")
  fun processDebugCommand(
      @RequestParam settingsId: UUID,
      @RequestParam connectionId: UUID,
      @RequestBody listOfCompanies: List<CompanyInfo> // Changed to Body for easier testing
  ): CompletableFuture<Any> {
    return commandGateway.send(
        MarkListOfCompaniesFetchedCommand(settingsId, connectionId, listOfCompanies))
  }

  /** MAIN ROUTE */
  @CrossOrigin
  @PostMapping("/fetchcompanieslistfromboond/{settingsId}")
  fun processCommand(
      @PathVariable("settingsId") settingsId: UUID,
      @RequestBody payload: FetchCompaniesListFromBoondPayload
  ): CompletableFuture<Any> {
    logger.info { "Manually marking companies fetched for settings $settingsId" }

    return commandGateway.send(
        MarkListOfCompaniesFetchedCommand(
            settingsId = settingsId,
            connectionId = payload.connectionId,
            listOfCompanies = payload.listOfCompanies))
  }
}
