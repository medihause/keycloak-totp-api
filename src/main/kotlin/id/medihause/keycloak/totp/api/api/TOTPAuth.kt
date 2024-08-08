package id.medihause.keycloak.totp.api.api

import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.keycloak.models.KeycloakSession
import org.keycloak.services.managers.AppAuthManager

class TOTPAuth(
    private val session: KeycloakSession,
) {
    @Path("manage-totp/{userId}")
    fun getTOTPResource(
        @PathParam("userId") userId: String
    ): TOTPResourceApi {
        val auth = AppAuthManager.BearerTokenAuthenticator(session).authenticate()

        if (auth == null) {
            throw NotAuthorizedException("Bearer token not found")
        } else if (auth.user.serviceAccountClientLink == null) {
            throw NotAuthorizedException("User is not a service account")
        } else if (auth.token.realmAccess == null || !auth.token.realmAccess.isUserInRole("admin")) {
            throw NotAuthorizedException("User is not an admin")
        }

        val user = session.users().getUserById(session.context.realm, userId)
            ?: throw NotAuthorizedException("User not found")

        if (user.serviceAccountClientLink != null) {
            throw NotAuthorizedException("Cannot manage service account")
        }

        return TOTPResourceApi(session, user)
    }
}