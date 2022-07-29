package ru.cobalt42.auth.routes

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import ru.cobalt42.auth.dto.Authorization
import ru.cobalt42.auth.dto.RefreshData
import ru.cobalt42.auth.service.AuthorizationService


fun Route.authRouting() {

    val authorizationService: AuthorizationService by inject()
    route("/api/auth") {
        post("/generate") {
            val isAdminPanel = call.request.queryParameters["isAdminPanel"]?.toBoolean() ?: false
            val authorization = call.receive<Authorization>()
            call.respond(authorizationService.generate(authorization, isAdminPanel))
        }
        post("/refresh") {
            val refresh = call.receive<RefreshData>()
            call.respond(authorizationService.refresh(refresh))
        }
    }
}

fun Application.registerAuthRoutes() {
    routing {
        authRouting()
    }
}
