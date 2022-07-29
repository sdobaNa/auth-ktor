package ru.cobalt42.auth.service

import io.ktor.http.*
import org.mindrot.jbcrypt.BCrypt
import ru.cobalt42.auth.dto.PaginatedResponse
import ru.cobalt42.auth.exception.EmptyResultDataAccessException
import ru.cobalt42.auth.exception.RequestException
import ru.cobalt42.auth.exception.ValidateException
import ru.cobalt42.auth.modal.Refresh
import ru.cobalt42.auth.modal.dictionary.ExceptionMessage
import ru.cobalt42.auth.modal.user.User
import ru.cobalt42.auth.refreshTime
import ru.cobalt42.auth.repository.refresh.RefreshRepository
import ru.cobalt42.auth.repository.user.UserRepository
import ru.cobalt42.auth.util.SystemMessages
import java.text.SimpleDateFormat
import java.util.*

class UserServiceImpl(
    private val repository: UserRepository,
    private val refreshRepository: RefreshRepository,
    private val systemMessages: SystemMessages
) : UserService {
    override suspend fun createOne(user: User, authToken: String): User {
        val messages = validator(user, authToken)
        if (user.password.isBlank())
            messages.add(
                systemMessages.getWarning(
                    authToken = authToken,
                    uname = "requiredFieldsEmpty",
                    description = "Пароль"
                )
            )
        if (user.login.isNotBlank())
            try {
                repository.findByLogin(user.login)
                messages.add(
                    systemMessages.getException(
                        authToken = authToken,
                        uname = "loginIsUse"
                    )
                )
                throw ValidateException(messages, user)
            } catch (e: EmptyResultDataAccessException) {
                user.uid = UUID.randomUUID().toString()
                user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
                repository.save(user)
                refreshRepository.save(
                    Refresh(
                        refresh = UUID.randomUUID().toString(),
                        exp = try {
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date(System.currentTimeMillis() + refreshTime.toInt()))
                        } catch (e: Throwable) {
                            throw RequestException("Expiration date exception", HttpStatusCode.BadRequest)
                        },
                        user = user.uid
                    )
                )
            }
        return user
    }

    override suspend fun getAll(skip: Int, size: Int, search: String): PaginatedResponse<User> {
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
                repository.findByLoginContainingIgnoreCase(search, skip, size)
                    .also { total = repository.getCountAll() }.toList(),
                total = total
            )
    }

    override suspend fun getOne(uid: String): User = repository.findByUid(uid).also { it.password = "" }

    override suspend fun updateOne(uid: String, user: User, authToken: String): User {
        user.uid = uid
        val messages = validator(user, authToken)
        if (messages.any { (it.code in 1..9999) })
            throw ValidateException(messages, user)
        val old = try {
            repository.findByUid(uid)
        } catch (e: EmptyResultDataAccessException) {
            messages.add(
                systemMessages.getWarning(
                    authToken = authToken,
                    uname = "updatedDocumentNotFound"
                )
            )
            User()
        }
        if (user.password.isNotBlank())
            user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        else user.password = old.password
        user._id = old._id
        repository.save(user)
        return user
    }

    override suspend fun deleteOne(uid: String) = repository.deleteByUid(uid)

    private suspend fun validator(user: User, authToken: String): MutableList<ExceptionMessage> {
        val messages = mutableListOf<ExceptionMessage>()
        if (user.login.isBlank())
            messages.add(
                systemMessages.getWarning(
                    authToken = authToken,
                    uname = "requiredFieldsEmpty",
                    description = "Логин"
                )
            )
        if (user.roles.isEmpty())
            messages.add(
                systemMessages.getWarning(
                    authToken = authToken,
                    uname = "requiredFieldsEmpty",
                    description = "Роли"
                )
            )
        return messages
    }
}