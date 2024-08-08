package id.medihause.keycloak.totp.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterTOTPCredentialRequest (
    @SerialName("deviceName")
    val deviceName: String,

    @SerialName("encodedSecret")
    val encodedSecret: String,

    @SerialName("initialCode")
    val initialCode: String,

    @SerialName("overwrite")
    val overwrite: Boolean = false
) {
    companion object {
        fun validate(request: RegisterTOTPCredentialRequest): Boolean {
            return request.deviceName.isNotEmpty() && request.encodedSecret.isNotEmpty() && request.initialCode.isNotEmpty()
        }
    }
}