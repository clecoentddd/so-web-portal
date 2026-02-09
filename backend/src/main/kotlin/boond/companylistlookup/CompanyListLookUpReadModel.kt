package boond.companylistlookup

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable

/** QUERY: Simple object to trigger the fetch of all companies. */
class GetAllCompaniesQuery : Serializable

/** ENTITY: Flat table structure where companyId is the Primary Key. */
@Entity
@Table(name = "company_list_lookup")
class CompanyListLookUpReadModelEntity : Serializable {
  @Id var companyId: Long = 0

  var companyName: String = ""

  // Default constructor for JPA
  constructor()

  constructor(companyId: Long, companyName: String) {
    this.companyId = companyId
    this.companyName = companyName
  }
}

data class GetCompanyByIdQuery(val companyId: Long) : Serializable

/** RESPONSE WRAPPER: To ensure the API returns a structured JSON object. */
data class CompanyListLookUpResponse(val companies: List<CompanyListLookUpReadModelEntity>)
