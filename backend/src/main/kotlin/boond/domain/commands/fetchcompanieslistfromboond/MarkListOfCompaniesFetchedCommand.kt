package boond.domain.commands.fetchcompanieslistfromboond

import boond.common.Command
import boond.common.CompanyInfo // Import our shared class
import java.util.UUID
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class MarkListOfCompaniesFetchedCommand(
    @TargetAggregateIdentifier val settingsId: UUID,
    val connectionId: UUID,
    val listOfCompanies: List<CompanyInfo> = emptyList() // Consistent naming
) : Command
