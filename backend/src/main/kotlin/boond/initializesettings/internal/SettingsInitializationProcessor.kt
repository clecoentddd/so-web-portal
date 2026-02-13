package boond.requestinvoicestatemapping.internal

import boond.common.SettingsConstants
import boond.common.SystemActors
import boond.domain.commands.initializesettings.InitializeSettingsCommand
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.command.AggregateStreamCreationException
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SettingsInitializerProcessor(private val commandGateway: CommandGateway) {

  private val logger = KotlinLogging.logger {}

  @EventListener(ApplicationReadyEvent::class)
  fun initSettingsAggregate() {
    logger.info {
      "Initializing SettingsAggregate with Settings ID: ${SettingsConstants.SETTINGS_ID}"
    }
    logger.info {
      "Initializing SettingsAggregate with System Actor ID: ${SystemActors.SYSTEM_PROCESSOR}"
    }

    commandGateway
        .send<Void>(
            InitializeSettingsCommand(SettingsConstants.SETTINGS_ID, SystemActors.SYSTEM_PROCESSOR))
        .whenComplete { _, ex ->
          if (ex != null) {
            if (ex.cause is AggregateStreamCreationException) {
              logger.info { "SettingsAggregate already exists, skipping creation." }
            } else {
              logger.error(ex) { "Error initializing SettingsAggregate!" }
              throw ex
            }
          } else {
            logger.info { "SettingsAggregate initialized successfully." }
          }
        }
  }
}
