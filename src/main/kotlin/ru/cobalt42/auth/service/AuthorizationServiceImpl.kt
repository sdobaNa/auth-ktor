package ru.cobalt42.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt
import ru.cobalt42.auth.accessTime
import ru.cobalt42.auth.dto.Authorization
import ru.cobalt42.auth.dto.DefaultResponse
import ru.cobalt42.auth.dto.RefreshData
import ru.cobalt42.auth.exception.EmptyResultDataAccessException
import ru.cobalt42.auth.exception.RequestException
import ru.cobalt42.auth.modal.Refresh
import ru.cobalt42.auth.modal.role.Role
import ru.cobalt42.auth.modal.user.User
import ru.cobalt42.auth.refreshTime
import ru.cobalt42.auth.repository.refresh.RefreshRepository
import ru.cobalt42.auth.repository.role.RoleRepository
import ru.cobalt42.auth.repository.user.UserRepository
import ru.cobalt42.auth.util.enums.Permissions
import java.text.SimpleDateFormat
import java.util.*

class AuthorizationServiceImpl(
    private val userRepository: UserRepository,
    private val refreshRepository: RefreshRepository,
    private val roleRepository: RoleRepository,
) : AuthorizationService {
    override suspend fun generate(authorization: Authorization, isAdminPanel: Boolean): DefaultResponse<RefreshData> =
        try {
            val user = userRepository.findByLogin(authorization.login)
            if (user.disabled) throw RequestException("User is disabled", BadRequest)
            if (BCrypt.checkpw(authorization.password, user.password)) {
                generateJWT(user, isAdminPanel = isAdminPanel)
            } else {
                throw RequestException("Wrong password", BadRequest)
            }
        } catch (e: EmptyResultDataAccessException) {
            throw RequestException("Wrong login", BadRequest)
        }

    override suspend fun refresh(refreshData: RefreshData): DefaultResponse<RefreshData> = generateJWT(
        refresh = try {
            refreshRepository.findByRefresh(refreshData.refresh)
        } catch (e: EmptyResultDataAccessException) {
            throw RequestException("Expired or invalid JWT token", Unauthorized)
        }
    )

    private suspend fun generateJWT(
        user: User = User(),
        refresh: Refresh = Refresh(),
        isAdminPanel: Boolean = false
    ): DefaultResponse<RefreshData> {
        val refreshEntry = try {
            refreshRepository.findByRefresh(refresh.refresh)
        } catch (e: EmptyResultDataAccessException) {
            Refresh()
        }

        val foundUser = if (user.uid.isBlank()) {
            val payload = try {
                String(
                    Base64.getDecoder().decode(
                        refreshEntry.token.split(".")[1]
                    )
                )
            } catch (e: Throwable) {
                throw RequestException("Expired or invalid JWT token", Unauthorized)
            }
            try {
                userRepository.findByUid(JSONObject(payload)["user"].toString())
                    .also { if (it.disabled) throw RequestException("User is disabled", BadRequest) }
            } catch (e: EmptyResultDataAccessException) {
                throw RequestException("User not found", BadRequest)
            } catch (e: Throwable) {
                throw RequestException("Expired or invalid JWT token", Unauthorized)
            }
        } else user

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        try {
            if (refreshEntry.refresh.isNotBlank() && refreshEntry.exp.isNotBlank()) {
                if (dateFormatter.parse(refreshEntry.exp).time - Date(System.currentTimeMillis() + 1000000).time < 0)
                    throw RequestException("Expired or invalid JWT token", Unauthorized)
                else if (dateFormatter.parse(refreshEntry.exp).time - Date(System.currentTimeMillis() + 1000000).time >= 0) {
                    refreshEntry.refresh = UUID.randomUUID().toString()
                    refreshEntry.exp = dateFormatter.format(Date(System.currentTimeMillis() + refreshTime.toInt()))
                }
            } else {
                refreshEntry.refresh = UUID.randomUUID().toString()
                refreshEntry.exp = dateFormatter.format(Date(System.currentTimeMillis() + refreshTime.toInt()))
            }
        } catch (e: NumberFormatException) {
            throw RequestException("Invalid property token.refresh.time", BadRequest)
        } catch (e: RequestException) {
            throw e
        } catch (e: Throwable) {
            throw RequestException("Incorrect expiration date", BadRequest)
        }
        val userRoles = foundUser.roles.map {
            try {
                roleRepository.findByUid(it)
            } catch (e: EmptyResultDataAccessException) {
                throw RequestException("Role not found", Forbidden)
            }
        }

        if (isAdminPanel && userRoles?.find { it.name == "admin" } == null)
            throw RequestException("Access denied", BadRequest)

        val roles = rolesFormatter(userRoles!!)

        val token = try {
            JWT.create()
                .withClaim("permission", roles)
                .withClaim("user", foundUser.uid)
                .withIssuedAt(Date())
                .withExpiresAt(Date(System.currentTimeMillis() + accessTime.toInt())).sign(
                    Algorithm.HMAC256("secret")
                )
        } catch (e: NumberFormatException) {
            throw RequestException("Invalid property token.access.time", BadRequest)
        } catch (e: Throwable) {
            throw RequestException("Denied JWT create", BadRequest)
        }

        val userRefresh = try {
            refreshRepository.findByUser(foundUser.uid)
        } catch (e: EmptyResultDataAccessException) {
            throw RequestException("Refresh not found", BadRequest)
        }
        userRefresh.refresh = refreshEntry.refresh
        userRefresh.exp = refreshEntry.exp
        userRefresh.token = token
        refreshRepository.save(userRefresh)
        return DefaultResponse(
            RefreshData(
                userRefresh.refresh,
                token,
                user.uid,
                user.name,
                user.organization,
                user.position,
                user.avatar
            )
        )
    }

    private fun rolesFormatter(roles: List<Role>): Map<String, Int> {
        val rolesMap = mutableMapOf<String, Int>()
        Permissions.PERMISSIONS.permissions.forEach {
            rolesMap[it.uname] = it.permissionLevel
        }
        roles.forEach { role ->
            role.permissions.forEach { permission ->
                if (rolesMap[permission.uname] != null) {
                    if (rolesMap[permission.uname]!! < permission.permissionLevel)
                        rolesMap[permission.uname] = permission.permissionLevel
                } else throw RequestException("Permission is missing", Forbidden)
            }
        }
        return rolesMap
    }
}