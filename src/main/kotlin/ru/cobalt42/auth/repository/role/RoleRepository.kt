package ru.cobalt42.auth.repository.role

import ru.cobalt42.auth.modal.role.Role

interface RoleRepository {
    suspend fun findByUid(uid: String): Role
    suspend fun findAll(skip: Int, size: Int): List<Role>
    suspend fun getCountAll(): Long
    suspend fun findByNameContainingIgnoreCase(name: String, skip: Int, size: Int): List<Role>
    suspend fun getCountByName(name: String): Long
    suspend fun deleteByUid(uid: String)
    suspend fun save(data: Role)
}