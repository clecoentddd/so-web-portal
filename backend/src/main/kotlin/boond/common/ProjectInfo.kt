package boond.domain.models

import jakarta.persistence.Embeddable
import java.time.LocalDate

@Embeddable
data class ProjectInfo(
    var projectId: String = "",
    var projectTitle: String = "",
    var projectDescription: String = "",
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var forecastEndDate: LocalDate? = null,
    var status: String = ""
)
