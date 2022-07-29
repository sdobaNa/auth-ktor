package ru.cobalt42.auth.service

import ru.cobalt42.auth.dto.PaginatedResponse
import ru.cobalt42.auth.modal.user.User

interface UserService {
    suspend fun createOne(user: User, authToken: String): User
    suspend fun getAll(skip: Int, size: Int, search: String): PaginatedResponse<User>
    suspend fun getOne(uid: String): User
    suspend fun updateOne(uid: String, user: User, authToken: String): User
    suspend fun deleteOne(uid: String)
}