package id.medihause.keycloak.totp.api

import org.keycloak.Config
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.services.resource.RealmResourceProvider
import org.keycloak.services.resource.RealmResourceProviderFactory

class TOTPApiRealmResourceProviderFactory: RealmResourceProviderFactory {
    companion object {
        const val PROVIDER_ID = "totp-api"
    }

    override fun create(p0: KeycloakSession?): RealmResourceProvider {
        return TOTPApiRealmResourceProvider(p0!!)
    }

    override fun init(p0: Config.Scope?) {}

    override fun postInit(p0: KeycloakSessionFactory?) {}

    override fun close() {}

    override fun getId(): String {
        return PROVIDER_ID
    }
}