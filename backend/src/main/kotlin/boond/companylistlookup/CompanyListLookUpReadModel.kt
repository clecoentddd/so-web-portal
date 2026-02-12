package boond.companylistlookup

import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

/** QUERY: Simple object to trigger the fetch of all companies. */
class GetAllCompaniesQuery : Serializable

/** ENTITY: Flat table structure where companyId is the Primary Key. */
@Entity
@Table(name = "company_list_lookup")
class CompanyListLookUpReadModelEntity : Serializable {
  @Id @GeneratedValue var id: UUID? = null

  @Column(nullable = false) var companyId: Long = 0

  @Column(nullable = false) var settingsId: UUID? = null

  @Column(nullable = false) var connectionId: UUID? = null

  var companyName: String = ""

  @Column(nullable = false) var timestamp: Long = 0

  // Default constructor for JPA
  constructor()

  constructor(
          companyId: Long,
          companyName: String,
          settingsId: UUID?,
          connectionId: UUID?,
          timestamp: Long
  ) {
    this.companyId = companyId
    this.companyName = companyName
    this.settingsId = settingsId
    this.connectionId = connectionId
    this.timestamp = timestamp
  }
}

data class GetCompanyByIdQuery(val companyId: Long) : Serializable

/** RESPONSE WRAPPER: To ensure the API returns a structured JSON object. */
data class CompanyListLookUpResponse(val companies: List<CompanyListLookUpReadModelEntity>)
