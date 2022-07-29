package ru.cobalt42.auth.exception

import ru.cobalt42.auth.modal.dictionary.ExceptionMessage

data class ValidateException(
    val messages: List<ExceptionMessage>,
    val result: Any?,
) : Exception()