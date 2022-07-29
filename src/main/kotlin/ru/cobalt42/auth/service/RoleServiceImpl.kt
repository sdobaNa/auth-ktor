package ru.cobalt42.auth.service

import ru.cobalt42.auth.dto.PaginatedResponse
import ru.cobalt42.auth.exception.EmptyResultDataAccessException
import ru.cobalt42.auth.exception.ValidateException
import ru.cobalt42.auth.modal.dictionary.ExceptionMessage
import ru.cobalt42.auth.modal.role.Role
import ru.cobalt42.auth.repository.role.RoleRepository
import ru.cobalt42.auth.util.SystemMessages
import java.util.*

class RoleServiceImpl(private val repository: RoleRepository, private val systemMessages: SystemMessages) :
    RoleService {

    override suspend fun createOne(role: Role, authToken: String): Role {
        val messages = validator(role, authToken)
        if (messages.isEmpty()) {
            role.uid = UUID.randomUUID().toString()
            repository.save(role)
        } else
            throw ValidateException(messages, role)
        return role
    }

    override suspend fun getAll(skip: Int, size: Int, search: String): PaginatedResponse<Role> {
        var total: Long
        return if (search.isBlank())
            PaginatedResponse(
                result =
                repository.findAll(skip, size).also { total = repository.getCountAll() }
                    .toList(),
                total = total
            )
        else
            PaginatedResponse(
                result =
                repository.findByNameContainingIgnoreCase(search, skip, size)
                    .also { total = repository.getCountByName(search) }.toList(),
                total = total
            )
    }

    override suspend fun getOne(uid: String): Role = repository.findByUid(uid)

    override suspend fun updateOne(uid: String, role: Role, authToken: String): Role {
        role.uid = uid
        val messages = validator(role, authToken)
        if (messages.any { (it.code in 1..9999) })
            throw ValidateException(messages, role)
        val old = try {
            repository.findByUid(uid)
        } catch (e: EmptyResultDataAccessException) {
            messages.add(
                systemMessages.getWarning(
                    authToken = authToken,
                    uname = "updatedDocumentNotFound"
                )
            )
            Role()
        }
        role._id = old._id
        repository.save(role)
        return role
    }

    override suspend fun deleteOne(uid: String) = repository.deleteByUid(uid)

    private suspend fun validator(role: Role, authToken: String): MutableList<ExceptionMessage> {
        val message = mutableListOf<ExceptionMessage>()
        if (role.name.isBlank())
            message.add(
                systemMessages.getException(
                    authToken = authToken,
                    uname = "requiredFieldsEmpty",
                    description = "Наименование"
                )
            )
        return message
    }
}