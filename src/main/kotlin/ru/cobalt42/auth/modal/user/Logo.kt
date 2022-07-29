package ru.cobalt42.auth.modal.user

import kotlinx.serialization.Serializable

@Serializable
data class Logo(
    var originName: String = "",
    var fileName: String = ""
)