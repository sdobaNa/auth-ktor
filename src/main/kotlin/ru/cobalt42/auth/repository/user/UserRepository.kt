package ru.cobalt42.auth.repository.user

import ru.cobalt42.auth.modal.user.User

interface UserRepository {
    suspend fun findByUid(uid: String): User
    suspend fun findByLogin(login: String): User
    suspend fun findAll(skip: Int, size: Int): List<User>
    suspend fun getCountAll(): Long
    suspend fun findByLoginContainingIgnoreCase(login: String, skip: Int, size: Int): List<User>
    suspend fun getCountByName(name: String): Long
    suspend fun deleteByUid(uid: String)
    suspend fun save(data: User)
}