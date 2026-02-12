package boond.invoicestatemapping

import jakarta.persistence.*
import java.util.UUID

/** Stores invoice state mappings for a given settingsId. */
@Entity
@Table(name = "invoice_state_mapping")
data class InvoiceStateMappingProjectionEntity(
    @Id @GeneratedValue var id: UUID? = null,
    @Column(nullable = false) var settingsId: UUID,
    @Column(nullable = false) var code: Int,
    @Column(nullable = false) var label: String,
    @Column(nullable = false) var connectionId: UUID,
    @Column(nullable = false) var timestamp: Long
)
