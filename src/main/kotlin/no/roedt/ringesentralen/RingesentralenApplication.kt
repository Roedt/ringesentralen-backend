package no.roedt.ringesentralen

import jakarta.annotation.security.DeclareRoles
import jakarta.ws.rs.core.Application
import org.eclipse.microprofile.auth.LoginConfig
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.info.License
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme

@OpenAPIDefinition(
    info =
        Info(
            title = "Ringesentral-API",
            version = "1.0.0",
            contact =
                Contact(
                    name = "Mads Opheim",
                    url = "https://github.com/roedt/ringesentralen-backend"
                ),
            license =
                License(
                    name = "MIT License",
                    url = "https://github.com/Roedt/ringesentralen-backend/blob/main/LICENSE"
                )
        )
)
@SecurityScheme(
    securitySchemeName = "jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "jwt"
)
@LoginConfig(authMethod = "MP-JWT", realmName = "jwt-jaspi")
@jakarta.annotation.security.DeclareRoles("ringer")
class RingesentralenApplication : Application()
