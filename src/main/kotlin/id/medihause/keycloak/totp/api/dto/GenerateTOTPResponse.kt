package id.medihause.keycloak.totp.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateTOTPResponse(
    @SerialName("encodedSecret")
    val encodedSecret: String,

    @SerialName("qrCode")
    val qrCode: String
)