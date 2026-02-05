package boond.fetchcompanieslistfromboond.internal

import boond.common.CompanyInfo // Import the new shared class
import boond.common.Processor
import boond.domain.commands.fetchcompanieslistfromboond.MarkListOfCompaniesFetchedCommand
import boond.events.AdminConnectedEvent
import boond.fetchcompanieslistfromboond.internal.adapter.FetchBoondAPIListeDesCompanies
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProcessorFetchBoondCompaniesListProcessor : Processor {
  private val logger = KotlinLogging.logger {}

  @Autowired lateinit var commandGateway: CommandGateway
  @Autowired lateinit var boondAdapter: FetchBoondAPIListeDesCompanies

  @EventHandler
  fun on(event: AdminConnectedEvent) {
    logger.info { "Processing AdminConnectedEvent for connection: ${event.connectionId}" }

    // 1. Fetch the data from the adapter
    val companiesInfo = boondAdapter.fetchAll()

    // 2. Map to our new shared CompanyInfo class
    val companiesList =
        companiesInfo.companies.map {
          CompanyInfo(companyId = it.companyId, companyName = it.companyName)
        }

    logger.info {
      "Sending MarkListOfCompaniesFetchedCommand for connection: ${event.connectionId} with ${companiesList.size} companies"
    }

    // 3. Send the command with corrected property name (listOfCompanies)
    commandGateway.send<Any>(
        MarkListOfCompaniesFetchedCommand(
            connectionId = event.connectionId, // Ensure non-null from event
            adminEmail = event.adminEmail,
            listOfCompanies = companiesList))
  }
}
