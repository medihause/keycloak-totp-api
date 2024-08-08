package id.medihause.keycloak.totp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class RegisterTOTPCredentialRequest(
    @JsonProperty("deviceName")
    val deviceName: String,

    @JsonProperty("encodedSecret")
    val encodedSecret: String,

    @JsonProperty("initialCode")
    val initialCode: String,

    @JsonProperty("overwrite")
    val overwrite: Boolean = false
) {
    companion object {
        fun validate(request: RegisterTOTPCredentialRequest): Boolean {
            return request.deviceName.isNotEmpty() && request.encodedSecret.isNotEmpty() && request.initialCode.isNotEmpty()
        }
    }
}