package boond.fetchcompanieslistfromboond.internal

import boond.common.CompanyInfo
import boond.common.Processor
import boond.domain.commands.fetchcompanieslistfromboond.MarkListOfCompaniesFetchedCommand
import boond.events.AdminConnectedEvent
import boond.fetchcompanieslistfromboond.internal.adapter.FetchBoondAPICompanyList
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238302
*/
@Component
class ProcessorFetchBoondCompaniesListProcessor(
    private val commandGateway: CommandGateway,
    private val boondAdapter: FetchBoondAPICompanyList
) : Processor {

  private val logger = KotlinLogging.logger {}

  @EventHandler
  fun on(event: AdminConnectedEvent) {
    logger.info {
      "Admin ${event.adminEmail} connected. Fetching companies for connection ${event.connectionId}..."
    }

    // 1. Fetch the data from the adapter
    // Note: Check if your adapter uses .fetch() or .fetchAll() to be consistent
    val companiesInfo = boondAdapter.fetchAll()

    // 2. Map to our shared CompanyInfo class
    // Explicitly naming the parameter (e.g., 'c') and providing the type
    // ensures Kotlin doesn't lose track of the properties.
    val companiesList =
        companiesInfo.companies.map { c ->
          CompanyInfo(companyId = c.companyId, companyName = c.companyName)
        }

    logger.info {
      "Dispatching MarkListOfCompaniesFetchedCommand for connection: ${event.connectionId} (${companiesList.size} companies found)"
    }

    // 3. Send the command with error handling
    commandGateway
        .send<Any>(
            MarkListOfCompaniesFetchedCommand(
                connectionId = event.connectionId,
                adminEmail = event.adminEmail,
                listOfCompanies = companiesList))
        .exceptionally { throwable ->
          logger.error(throwable) {
            "FAILED to mark company list for connection ${event.connectionId}. " +
                "Reason: ${throwable.message}"
          }
          null
        }
  }
}
