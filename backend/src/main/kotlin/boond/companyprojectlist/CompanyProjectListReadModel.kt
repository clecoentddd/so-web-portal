package boond.companyprojectlist

import boond.common.Event
import boond.common.Query
import boond.common.ReadModel
import boond.domain.models.ProjectInfo
import boond.events.ListOfProjectsFetchedEvent
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import java.util.UUID

@Entity
class CompanyProjectListReadModelEntity {

  @Id var sessionId: UUID? = null

  var companyId: Long? = null
  var customerId: UUID? = null

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "projection_session_projects", joinColumns = [JoinColumn(name = "session_id")])
  var projectList: MutableList<ProjectInfo> = mutableListOf()
}

data class CompanyProjectListReadModelQuery(val sessionId: UUID) : Query

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238316
*/
class CompanyProjectListReadModel : ReadModel {

  var sessionId: UUID? = null
  var companyId: Long? = null
  var customerId: UUID? = null

  var projectList: List<ProjectInfo> = emptyList()

  fun applyEvents(events: List<Event>): CompanyProjectListReadModel {
    events.forEach { event ->
      when (event) {
        is ListOfProjectsFetchedEvent -> {
          this.sessionId = event.sessionId
          this.companyId = event.companyId
          this.customerId = event.customerId
          this.projectList = event.projectList
        }
      }
    }
    return this
  }

  fun applyEventsFiltered(events: List<Event>, projectId: Long): CompanyProjectListReadModel {
    this.applyEvents(events)
    this.projectList = this.projectList.filter { it.projectId == projectId }
    return this
  }
}
