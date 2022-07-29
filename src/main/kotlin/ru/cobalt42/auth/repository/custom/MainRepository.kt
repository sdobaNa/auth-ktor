package ru.cobalt42.auth.repository.custom

import ru.cobalt42.auth.modal.dictionary.ExceptionMessage

interface MainRepository {
    suspend fun getMessages(authToken: String): Array<ExceptionMessage>
}