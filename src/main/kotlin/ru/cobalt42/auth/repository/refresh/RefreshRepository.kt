package ru.cobalt42.auth.repository.refresh

import ru.cobalt42.auth.modal.Refresh

interface RefreshRepository {
    suspend fun findByRefresh(refresh: String): Refresh
    suspend fun findByUser(user: String): Refresh
    suspend fun save(data: Refresh)
}