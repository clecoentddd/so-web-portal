package boond.companylistlookup.internal

import boond.companylistlookup.CompanyListLookUpReadModelEntity
import boond.companylistlookup.CompanyListLookUpResponse
import boond.companylistlookup.GetAllCompaniesQuery
import boond.companylistlookup.GetCompanyByIdQuery
import java.util.concurrent.CompletableFuture
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CompanylistlookupResource(private val queryGateway: QueryGateway) {

  @CrossOrigin
  @GetMapping("/companylistlookup")
  fun getAllCompanies(): CompletableFuture<CompanyListLookUpResponse> {
    // We query for multiple instances of the Entity
    return queryGateway
        .query(
            GetAllCompaniesQuery(),
            ResponseTypes.multipleInstancesOf(CompanyListLookUpReadModelEntity::class.java))
        .thenApply { companies ->
          // Wrap the list in our Response object to match frontend expectations
          CompanyListLookUpResponse(companies = companies)
        }
  }

  @CrossOrigin
  @GetMapping("/companylistlookup/{companyId}")
  fun getCompanyById(
      @PathVariable("companyId") companyId: Long
  ): CompletableFuture<CompanyListLookUpReadModelEntity> {
    return queryGateway.query(
        GetCompanyByIdQuery(companyId),
        ResponseTypes.instanceOf(CompanyListLookUpReadModelEntity::class.java))
  }
}
