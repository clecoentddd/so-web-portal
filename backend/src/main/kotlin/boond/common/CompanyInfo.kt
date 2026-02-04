package boond.common

/**
 * Shared domain info for both Commands and Events. Default values ensure Jackson can always
 * deserialize this.
 */
data class CompanyInfo(val companyId: Long = 0, val companyName: String = "")
