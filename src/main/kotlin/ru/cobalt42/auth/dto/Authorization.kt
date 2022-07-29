package ru.cobalt42.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class Authorization(
    val login: String = "",
    val password: String = ""
)
