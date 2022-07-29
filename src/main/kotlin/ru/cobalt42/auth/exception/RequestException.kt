package ru.cobalt42.auth.exception

import io.ktor.http.*

class RequestException(override val message: String, private val httpStatus: HttpStatusCode) : Exception(message) {
    private val serialVersionUID = 1L

    fun getHttpStatus(): HttpStatusCode {
        return httpStatus
    }

    fun message(): String {
        return message
    }
}