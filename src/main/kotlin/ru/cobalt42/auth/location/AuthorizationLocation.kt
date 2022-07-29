package ru.cobalt42.auth.location

import io.ktor.locations.*
import ru.cobalt42.auth.dto.Authorization

@KtorExperimentalLocationsAPI
@Location("")
data class AuthorizationLocation(
    val isAdminPanel: Boolean = false,
    val authorization: Authorization = Authorization()
)
