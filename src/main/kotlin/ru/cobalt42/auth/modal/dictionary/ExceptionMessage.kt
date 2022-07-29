package ru.cobalt42.auth.modal.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionMessage(
    val code: Int = 0,
    val uname: String = "",
    val title: String = "",
    var description: String = "",
    val target: Target = Target(),
    val source: Target = Target(),
    var section: String = "",
)
