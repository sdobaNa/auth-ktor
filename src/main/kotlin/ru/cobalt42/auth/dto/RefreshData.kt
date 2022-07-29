package ru.cobalt42.auth.dto

import kotlinx.serialization.Serializable
import ru.cobalt42.auth.modal.user.Logo

@Serializable
data class RefreshData(
    val refresh: String = "",
    val token: String = "",
    val uid: String = "",
    val userName: String = "",
    val organization: String = "",
    val position: String = "",
    val avatar: Logo = Logo()
)
