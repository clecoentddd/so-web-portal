package boond.customersessions.internal

import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/internal/system")
class CustomerSessionsManagementResource(
    private val eventProcessingConfiguration: EventProcessingConfiguration
) {

  @PostMapping("/rebuild-sessions")
  fun rebuildCustomerSessions() {
    // We target the package name because Axon defaults the "Processing Group"
    // to the package name of the Projector.
    eventProcessingConfiguration
        .eventProcessorByProcessingGroup<TrackingEventProcessor>("boond.customersessions.internal")
        .ifPresent { processor ->
          processor.shutDown()
          processor.resetTokens()
          processor.start()
        }
  }
}
