package ru.cobalt42.auth.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.configureSecurity() {
    val secret = environment.config.property("jwt.secret").getString()

    authentication {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
            )

            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }

}

