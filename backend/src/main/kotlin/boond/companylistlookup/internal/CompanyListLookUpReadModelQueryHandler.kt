package boond.companylistlookup.internal

import boond.companylistlookup.CompanyListLookUpReadModelEntity
import boond.companylistlookup.GetAllCompaniesQuery
import boond.companylistlookup.GetCompanyByIdQuery
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class CompanyListLookUpReadModelQueryHandler(
        private val repository: CompanyListLookUpReadModelRepository
) {

  @QueryHandler
  fun handle(query: GetAllCompaniesQuery): List<CompanyListLookUpReadModelEntity> {
    // Simply return every row in the lookup table
    return repository.findAll()
  }

  @QueryHandler
  fun handle(query: GetCompanyByIdQuery): CompanyListLookUpReadModelEntity? {
    return repository.findByCompanyId(query.companyId)
  }
}
