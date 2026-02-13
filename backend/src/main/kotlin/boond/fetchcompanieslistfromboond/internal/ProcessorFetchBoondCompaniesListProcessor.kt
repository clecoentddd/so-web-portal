package boond.fetchcompanieslistfromboond.internal

import boond.common.CompanyInfo
import boond.common.Processor
import boond.domain.commands.fetchcompanieslistfromboond.MarkListOfCompaniesFetchedCommand
import boond.events.*
import boond.fetchcompanieslistfromboond.internal.adapter.FetchBoondAPICompanyList
import java.util.UUID
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
  fun on(event: SettingsInitializedEvent) {
    logger.info {
      "Settings ${event.settingsId} initialized. Fetching companies for connection ${event.connectionId}..."
    }
    fetchAndDispatch(event.settingsId, event.connectionId)
  }

  @EventHandler
  fun on(event: CompanyListUpdateRequestedEvent) {
    logger.info { "Company list update requested for settings ${event.settingsId}. Fetching..." }
    fetchAndDispatch(event.settingsId, event.connectionId)
  }

  private fun fetchAndDispatch(settingsId: UUID, connectionId: UUID) {
    val companiesInfo = boondAdapter.fetchAll()
    val companiesList =
        companiesInfo.companies.map { c ->
          CompanyInfo(companyId = c.companyId, companyName = c.companyName)
        }

    logger.info {
      "Dispatching MarkListOfCompaniesFetchedCommand for settings: $settingsId (${companiesList.size} companies found)"
    }

    commandGateway
        .send<Any>(
            boond.domain.commands.fetchcompanieslistfromboond.MarkListOfCompaniesFetchedCommand(
                settingsId = settingsId,
                connectionId = connectionId,
                listOfCompanies = companiesList))
        .exceptionally { throwable ->
          logger.error(throwable) {
            "FAILED to mark company list for settings $settingsId. Reason: ${throwable.message}"
          }
          null
        }
  }
}
