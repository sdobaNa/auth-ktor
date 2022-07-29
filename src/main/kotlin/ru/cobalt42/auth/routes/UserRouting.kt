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
import ru.cobalt42.auth.modal.user.User
import ru.cobalt42.auth.service.UserService


fun Route.userRouting() {

    val userService: UserService by inject()
    authenticate("auth-jwt") {
        route("/api/auth/user") {
            post {
                val authToken = call.request.headers["Authorization"] ?: ""
                val user = call.receive<User>()
                call.respond(userService.createOne(user, authToken))
            }
            post("/{uid}") {
                val uid = call.parameters["uid"] ?: ""
                val authToken = call.request.headers["Authorization"] ?: ""
                val user = call.receive<User>()
                call.respond(userService.updateOne(uid, user, authToken))
            }
            get<PagingLocation> { location ->
                val skip = (location.page - 1) * location.size
                call.respond(userService.getAll(skip, location.size, location.search))
            }
            get("/{uid}") {
                val uid = call.parameters["uid"] ?: ""
                call.respond(userService.getOne(uid))
            }
            delete("/{uid}") {
                val uid = call.parameters["uid"] ?: ""
                userService.deleteOne(uid)
                call.respondText("", status = NoContent)
            }
        }
    }
}

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}
