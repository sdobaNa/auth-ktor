package ru.cobalt42.auth.dto

import kotlinx.serialization.Serializable
import ru.cobalt42.auth.modal.dictionary.ExceptionMessage

@Serializable
data class ValidateExceptionResponse<T>(
    val messages: List<ExceptionMessage>,
    val result: T
)