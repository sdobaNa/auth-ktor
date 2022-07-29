package ru.cobalt42.auth.util

import ru.cobalt42.auth.modal.dictionary.ExceptionMessage
import ru.cobalt42.auth.modal.dictionary.Target
import ru.cobalt42.auth.repository.custom.MainRepository

class SystemMessages(private val mainRepository: MainRepository) {

    suspend fun getException(
        authToken: String, uname: String, target: Target = Target(),
        source: Target = Target(), description: String = "", section: String = ""
    ): ExceptionMessage {
        val exceptions = mainRepository.getMessages(authToken)
        return exceptions.first { it.uname == uname && it.code < 10000 }
            .also {
                it.description = description.ifBlank { "" }
                it.target.uid = target.uid.ifBlank { "" }
                it.target.uname = target.uname.ifBlank { "" }
                it.target.serviceUname = target.serviceUname.ifBlank { "" }
                it.target.label = target.label.ifBlank { "" }
                it.source.uid = source.uid.ifBlank { "" }
                it.source.uname = source.uname.ifBlank { "" }
                it.source.serviceUname = source.serviceUname.ifBlank { "" }
                it.source.label = source.label.ifBlank { "" }
                it.section = section.ifBlank { "" }
            }
    }

    suspend fun getWarning(
        authToken: String, uname: String, target: Target = Target(),
        source: Target = Target(), description: String = "", section: String = ""
    ): ExceptionMessage {
        val exceptions = mainRepository.getMessages(authToken)
        return exceptions.first { it.uname == uname && it.code >= 10000 }
            .also {
                it.description = description.ifBlank { "" }
                it.target.uid = target.uid.ifBlank { "" }
                it.target.uname = target.uname.ifBlank { "" }
                it.target.serviceUname = target.serviceUname.ifBlank { "" }
                it.target.label = target.label.ifBlank { "" }
                it.source.uid = source.uid.ifBlank { "" }
                it.source.uname = source.uname.ifBlank { "" }
                it.source.serviceUname = source.serviceUname.ifBlank { "" }
                it.source.label = source.label.ifBlank { "" }
                it.section = section.ifBlank { "" }
            }
    }
}