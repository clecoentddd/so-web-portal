package boond.companyinvoices

import boond.common.InvoiceInfo
import jakarta.persistence.*
import java.util.UUID

// 1. The Query
data class InvoiceListReadModelQuery(val sessionId: UUID)

// 2. The Database Entity
@Entity
class InvoiceListReadModelEntity {
        @Id @Column(name = "session_id") var sessionId: UUID? = null
        var companyId: Long? = null

        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(
                name = "company_invoice_list_items",
                joinColumns = [JoinColumn(name = "session_id")]
        )
        var invoiceList: MutableList<InvoiceInfo> = mutableListOf()
}

// 3. The API Response Wrapper
class InvoiceListReadModel(entity: InvoiceListReadModelEntity) {
        var sessionId: UUID? = entity.sessionId
        var companyId: Long? = entity.companyId
        var invoiceList: List<InvoiceInfo> = entity.invoiceList.toList()
}
