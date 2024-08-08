package id.medihause.keycloak.totp.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyTOTPRequest(
    @SerialName("deviceName")
    val deviceName: String,

    @SerialName("code")
    val code: String
) {
    companion object {
        fun validate(request: VerifyTOTPRequest): Boolean {
            return request.deviceName.isNotEmpty() && request.code.isNotEmpty()
        }
    }
}