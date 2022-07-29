package ru.cobalt42.auth

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.server.netty.*
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.koin.ktor.ext.Koin
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.id.toId
import org.litote.kmongo.reactivestreams.KMongo
import ru.cobalt42.auth.config.configureSecurity
import ru.cobalt42.auth.exception.EmptyResultDataAccessException
import ru.cobalt42.auth.exception.RequestException
import ru.cobalt42.auth.exception.ValidateException
import ru.cobalt42.auth.modal.Refresh
import ru.cobalt42.auth.modal.role.Role
import ru.cobalt42.auth.modal.user.User
import ru.cobalt42.auth.routes.registerAuthRoutes
import ru.cobalt42.auth.routes.registerRoleRoutes
import ru.cobalt42.auth.routes.registerUserRoutes
import ru.cobalt42.auth.util.enums.Permissions
import ru.cobalt42.auth.util.writeLog
import java.text.SimpleDateFormat
import java.util.*

lateinit var mongoHost: String
lateinit var refreshTime: String
lateinit var accessTime: String
lateinit var testDB: String

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.main() {
    mongoHost = environment.config.property("mongo.host").getString()
    refreshTime = environment.config.property("jwt.refresh").getString()
    accessTime = environment.config.property("jwt.access").getString()
    testDB = environment.config.property("mongo.testDB").getString()

    install(Koin) {
        modules(fileAppModule)
    }
    install(DefaultHeaders)
    install(Authentication)
    install(Locations)
    install(CORS) {
        anyHost()
        header(HttpHeaders.ContentType)
        header(HttpHeaders.Authorization)
        method(HttpMethod.Options)
        method(HttpMethod.Delete)
    }
    install(ContentNegotiation) {
        gson {}
    }
    install(StatusPages) {
        exception<ValidateException> { ex ->
            call.respond(HttpStatusCode.BadRequest, ex)
        }
        exception<EmptyResultDataAccessException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<RequestException> { ex ->
            call.respond(ex)
        }
    }
    install(CallLogging) {
        format { call ->
            val status = call.response.status()?.value.toString()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val uri = call.request.uri

            writeLog(
                httpMethod,
                status,
                call.request.uri,
                call.request.header("Authorization"),
                call.request.receiveChannel().toString()
            )
            "Uri: $uri, Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }
    configureSecurity()
    registerRoleRoutes()
    registerUserRoutes()
    registerAuthRoutes()

    fun getUid() = UUID.randomUUID().toString()
    val userUid = getUid()
    val refreshUid = getUid()
    val roleUid = getUid()

    val client = KMongo.createClient("mongodb://$mongoHost:27017").coroutine
    val database = client.getDatabase("auth")
    val refreshCollection = database.getCollection<Refresh>()
    val roleCollection = database.getCollection<Role>()
    val userCollection = database.getCollection<User>()
    launch {
        userCollection.save(
            User(
                uid = userUid,
                disabled = false,
                login = "cobalt",
                password = "\$2a\$10\$2wggeB6Xl0tnHnMMOdd4vuANO/xcxd/h2iAZJCev48kgZ/gOeZMk.",
                name = "admin",
                roles = listOf(roleUid),
                _id = ObjectId("61c2d25a5c5ee051e9472242").toId(),
            )
        )

        roleCollection.save(
            Role(
                uid = roleUid,
                name = "admin",
                permissions = Permissions.PERMISSIONS.permissions.map { it.copy(permissionLevel = 4) },
                _id = ObjectId("6139c83a235ced2377be4f26").toId(),
            )
        )

        refreshCollection.save(
            Refresh(
                refresh = refreshUid,
                exp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date(System.currentTimeMillis() + 36000000)),
                token = "",
                user = userUid,
                _id = ObjectId("6139c983235ced2377be534c").toId(),
            )
        )
    }
}

suspend fun createAdmin() {
    fun getUid() = UUID.randomUUID().toString()
    val userUid = getUid()
    val refreshUid = getUid()
    val roleUid = getUid()

    val client = KMongo.createClient("mongodb://$mongoHost:27017").coroutine
    val database = client.getDatabase("auth")
    val refreshCollection = database.getCollection<Refresh>()
    val roleCollection = database.getCollection<Role>()
    val userCollection = database.getCollection<User>()

    userCollection.save(
        User(
            uid = userUid,
            disabled = false,
            login = "cobalt",
            password = "\$2a\$10\$2wggeB6Xl0tnHnMMOdd4vuANO/xcxd/h2iAZJCev48kgZ/gOeZMk.",
            name = "admin",
            roles = listOf(roleUid),
            _id = ObjectId("61c2d25a5c5ee051e9472242").toId(),
        )
    )

    roleCollection.save(
        Role(
            uid = roleUid,
            name = "admin",
            permissions = Permissions.PERMISSIONS.permissions.map { it.copy(permissionLevel = 4) },
            _id = ObjectId("6139c83a235ced2377be4f26").toId(),
        )
    )

    refreshCollection.save(
        Refresh(
            refresh = refreshUid,
            exp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date(System.currentTimeMillis() + 36000000)),
            token = "",
            user = userUid,
            _id = ObjectId("6139c983235ced2377be534c").toId(),
        )
    )
    client.close()
}
