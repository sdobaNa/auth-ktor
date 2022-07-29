package ru.cobalt42.auth.modal.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class Target(
    var label: String = "",
    var uname: String = "",
    var serviceUname: String = "",
    var uid: String = ""
)
