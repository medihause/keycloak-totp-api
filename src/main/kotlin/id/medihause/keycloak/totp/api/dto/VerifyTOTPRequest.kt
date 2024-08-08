package id.medihause.keycloak.totp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class VerifyTOTPRequest(
    @JsonProperty("deviceName")
    val deviceName: String,

    @JsonProperty("code")
    val code: String
) {
    companion object {
        fun validate(request: VerifyTOTPRequest): Boolean {
            return request.deviceName.isNotEmpty() && request.code.isNotEmpty()
        }
    }
}