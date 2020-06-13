package serializing

import kotlinx.serialization.Serializable

@Serializable
data class B(
    val id: Int,
    val str: String
)
