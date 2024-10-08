package id.medihause.keycloak.totp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize


@JsonSerialize
data class GenerateTOTPResponse(
    @JsonProperty("encodedSecret")
    val encodedSecret: String,

    @JsonProperty("qrCode")
    val qrCode: String
)