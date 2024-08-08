package id.medihause.keycloak.totp.api.api

import id.medihause.keycloak.totp.api.dto.CommonApiResponse
import id.medihause.keycloak.totp.api.dto.GenerateTOTPResponse
import id.medihause.keycloak.totp.api.dto.RegisterTOTPCredentialRequest
import id.medihause.keycloak.totp.api.dto.VerifyTOTPRequest
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.keycloak.credential.CredentialProvider
import org.keycloak.models.KeycloakSession
import org.keycloak.models.UserCredentialModel
import org.keycloak.models.UserModel
import org.keycloak.models.credential.OTPCredentialModel
import org.keycloak.models.utils.Base32
import org.keycloak.models.utils.HmacOTP
import org.keycloak.utils.CredentialHelper
import org.keycloak.utils.TotpUtils

class TOTPResourceApi(
    private val session: KeycloakSession,
    private val user: UserModel
) {
    private val totpSecretLength = 20

    @GET
    @Path("/generate-totp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun generateTOTP(): Response {
        val realm = session.context.realm

        val secret = HmacOTP.generateSecret(totpSecretLength)
        val qrCode = TotpUtils.qrCode(secret, realm, user)
        val encodedSecret = Base32.encode(secret.toByteArray())

        return Response.ok().entity(
            GenerateTOTPResponse(
                encodedSecret = encodedSecret,
                qrCode = qrCode
            )
        ).build()
    }

    @POST
    @Path("/verify-totp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun verifyTOTP(request: VerifyTOTPRequest): Response {
        if (!VerifyTOTPRequest.validate(request)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(CommonApiResponse("Invalid request")).build()
        }

        val credentialModel = user.credentialManager().getStoredCredentialByNameAndType(
            request.deviceName,
            OTPCredentialModel.TYPE
        )

        if (credentialModel == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(CommonApiResponse("TOTP credential not found"))
                .build()
        }

        val totpCredentialProvider = session.getProvider(CredentialProvider::class.java, "keycloak-otp")
        val totpCredentialModel = OTPCredentialModel.createFromCredentialModel(credentialModel)
        val credentialId = totpCredentialModel.id

        val isCredentialValid = user.credentialManager()
            .isValid(UserCredentialModel(credentialId, totpCredentialProvider.type, request.code))

        return if (isCredentialValid) {
            Response.ok().entity(CommonApiResponse("TOTP code is valid")).build()
        } else {
            Response.status(Response.Status.UNAUTHORIZED).entity(CommonApiResponse("Invalid TOTP code")).build()
        }
    }

    @POST
    @Path("/register-totp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun registerTOTP(request: RegisterTOTPCredentialRequest): Response {
        if (!RegisterTOTPCredentialRequest.validate(request)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(CommonApiResponse("Invalid request")).build()
        }

        val encodedTOTP = request.encodedSecret
        val secret = String(Base32.decode(encodedTOTP))

        if (secret.length != totpSecretLength) {
            return Response.status(Response.Status.BAD_REQUEST).entity(CommonApiResponse("Invalid secret")).build()
        }

        val realm = session.context.realm
        val credentialModel = user.credentialManager().getStoredCredentialByNameAndType(
            request.deviceName,
            OTPCredentialModel.TYPE
        )

        if (credentialModel != null && !request.overwrite) {
            return Response.status(Response.Status.CONFLICT).entity(CommonApiResponse("TOTP credential already exists"))
                .build()
        }

        val totpCredentialModel = OTPCredentialModel.createFromPolicy(realm, secret, request.deviceName)
        if (!CredentialHelper.createOTPCredential(session, realm, user, request.initialCode, totpCredentialModel)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(CommonApiResponse("Failed to create TOTP credential")).build()
        }

        return Response.status(Response.Status.CREATED).entity(CommonApiResponse("TOTP credential registered")).build()
    }
}