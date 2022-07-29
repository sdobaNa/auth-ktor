@file:OptIn(KtorExperimentalLocationsAPI::class)

package ru.cobalt42.auth.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import ru.cobalt42.auth.location.PagingLocation
import ru.cobalt42.auth.modal.role.Role
import ru.cobalt42.auth.service.RoleService


fun Route.roleRouting() {

    val roleService: RoleService by inject()
    authenticate("auth-jwt") {
        route("/api/auth/role") {
            post {
                val authToken = call.request.headers["Authorization"] ?: ""
                val role = call.receive<Role>()
                call.respond(roleService.createOne(role, authToken))
            }
            post("/{uid}") {
                val uid = call.parameters["uid"] ?: ""
                val authToken = call.request.headers["Authorization"] ?: ""
                val role = call.receive<Role>()
                call.respond(roleService.updateOne(uid, role, authToken))
            }
            get<PagingLocation> { location ->
                val skip = (location.page - 1) * location.size
                call.respond(roleService.getAll(skip, location.size, location.search))
            }
            get("/{uid}") {
                val uid = call.parameters["uid"] ?: ""
                call.respond(roleService.getOne(uid))
            }
            delete("/{uid}") {
                val uid = call.parameters["uid"] ?: ""
                roleService.deleteOne(uid)
                call.respondText("", status = NoContent)
            }
        }
    }
}

fun Application.registerRoleRoutes() {
    routing {
        roleRouting()
    }
}
