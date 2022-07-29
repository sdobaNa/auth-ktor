package ru.cobalt42.auth.dto

import kotlinx.serialization.Serializable
import ru.cobalt42.auth.modal.dictionary.ExceptionMessage

@Serializable
data class DefaultResponse<T>(
    var result: T,
    var messages: MutableList<ExceptionMessage> = mutableListOf(),
)