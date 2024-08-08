package id.medihause.keycloak.totp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class CommonApiResponse (
    @JsonProperty("message")
    val message: String
)