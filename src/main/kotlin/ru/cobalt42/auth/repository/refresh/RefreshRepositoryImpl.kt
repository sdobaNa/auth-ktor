package ru.cobalt42.auth.repository.refresh

import org.litote.kmongo.eq
import ru.cobalt42.auth.config.MongoInit
import ru.cobalt42.auth.exception.EmptyResultDataAccessException
import ru.cobalt42.auth.modal.Refresh

class RefreshRepositoryImpl : RefreshRepository {
    override suspend fun findByRefresh(refresh: String): Refresh =
        try {
            MongoInit.refreshCollection.findOne(Refresh::refresh eq refresh)!!
        } catch (e: Exception) {
            throw EmptyResultDataAccessException()
        }

    override suspend fun findByUser(user: String): Refresh =
        try {
            MongoInit.refreshCollection.findOne(Refresh::user eq user)!!
        } catch (e: Exception) {
            throw EmptyResultDataAccessException()
        }

    override suspend fun save(data: Refresh) {
        MongoInit.refreshCollection.save(data)
    }
}