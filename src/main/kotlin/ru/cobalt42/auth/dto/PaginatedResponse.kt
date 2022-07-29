package ru.cobalt42.auth.dto

import kotlinx.serialization.Serializable
import ru.cobalt42.auth.modal.dictionary.ExceptionMessage

@Serializable
data class PaginatedResponse<T>(
    var total: Long = 0,
    var result: List<T> = emptyList(),
    var messages: List<ExceptionMessage> = emptyList()
)
