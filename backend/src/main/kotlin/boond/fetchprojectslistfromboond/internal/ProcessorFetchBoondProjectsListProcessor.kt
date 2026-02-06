package boond.fetchprojectslistfromboond.internal

import boond.common.Processor
import boond.domain.commands.fetchprojectslistfromboond.MarkListOfProjectsFetchedCommand
import boond.domain.models.ProjectInfo
import boond.events.CustomerSessionInitiatedEvent
import boond.fetchprojectslistfromboond.internal.adapter.FetchBoondAPIProjectList
import boond.fetchprojectslistfromboond.internal.adapter.Projet
import java.time.LocalDate
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238302
*/
@Component
class ProcessorFetchBoondProjectsListProcessor(
    private val commandGateway: CommandGateway,
    private val boondAdapter: FetchBoondAPIProjectList
) : Processor {

  private val logger = KotlinLogging.logger {}

  @EventHandler
  fun on(event: CustomerSessionInitiatedEvent) {
    logger.info {
      "Session ${event.sessionId} initiated. Fetching projects from Boond for Company ${event.companyId}..."
    }

    // 1. Fetch data (returns the ProjetsListInfo wrapper)
    val response = boondAdapter.fetch(event.companyId)

    // 2. Map French Adapter DTOs to Common Domain Model
    // Explicitly typing 'p: Projet' solves the 'cannot infer type' error
    val projectsList: List<ProjectInfo> =
        response.projets.map { p: Projet ->
          ProjectInfo(
              projectId = p.projetId,
              projectTitle = p.projectTitle,
              projectDescription = p.projetDescription,
              startDate = p.startDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
              endDate = p.endDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
              forecastEndDate =
                  p.forecastEndDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
              status = p.status)
        }

    // 3. Dispatch command to the Aggregate
    commandGateway
        .send<Any>(
            MarkListOfProjectsFetchedCommand(
                sessionId = event.sessionId,
                companyId = event.companyId,
                customerId = event.customerId,
                projectList = projectsList))
        .exceptionally { throwable ->
          // Exceptionally handles the failure of the command execution
          logger.error(throwable) {
            "FAILED to mark projects for session ${event.sessionId}. " +
                "Reason: ${throwable.message}"
          }
          null // Return null to satisfy the exceptionally signature
        }

    logger.info {
      "Successfully dispatched project list (${projectsList.size} items) for session ${event.sessionId}"
    }
  }
}
