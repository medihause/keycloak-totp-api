package id.medihause.keycloak.totp.api

import id.medihause.keycloak.totp.api.api.TOTPAuth
import org.keycloak.models.KeycloakSession
import org.keycloak.services.resource.RealmResourceProvider

class TOTPApiRealmResourceProvider(
    private val session: KeycloakSession
): RealmResourceProvider {
    override fun close() {}

    override fun getResource(): TOTPAuth {
        return TOTPAuth(session)
    }
}