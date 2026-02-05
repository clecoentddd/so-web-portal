package boond.events

import java.util.UUID

/*
Boardlink: https://miro.com/app/board/uXjVIKUE2jo=/?moveToWidget=3458764658242238226
*/
// In boond.events package
data class CustomerConnectedEvent(
    val customerId: UUID,
    val clientEmail: String, // Ensure this exists here
    val companyId: Long
)
