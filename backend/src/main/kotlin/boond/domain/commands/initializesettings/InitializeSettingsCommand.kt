package boond.domain.commands.initializesettings

import boond.common.Command
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764659095873109
*/
data class InitializeSettingsCommand(
        @TargetAggregateIdentifier var settingsId: UUID,
        var connectionId: UUID,
) : Command
