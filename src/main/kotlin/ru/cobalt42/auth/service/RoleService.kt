package ru.cobalt42.auth.service

import ru.cobalt42.auth.dto.PaginatedResponse
import ru.cobalt42.auth.modal.role.Role

interface RoleService {
    suspend fun createOne(role: Role, authToken: String): Role
    suspend fun getAll(skip: Int, size: Int, search: String): PaginatedResponse<Role>
    suspend fun getOne(uid: String): Role
    suspend fun updateOne(uid: String, role: Role, authToken: String): Role
    suspend fun deleteOne(uid: String)
}