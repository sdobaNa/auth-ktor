package ru.cobalt42.auth.modal.role

import kotlinx.serialization.Serializable

@Serializable
data class Permission(
    var permissionLevel: Int = 0,
    val uname: String = "",
    val summary: String = ""
)
