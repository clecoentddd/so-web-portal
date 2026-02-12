package boond.common

import jakarta.persistence.Embeddable

@Embeddable data class InvoiceStateMappingInfo(var code: Int = 0, var label: String = "")
