package ru.cobalt42.auth.repository.role

import org.litote.kmongo.eq
import org.litote.kmongo.regex
import ru.cobalt42.auth.config.MongoInit
import ru.cobalt42.auth.exception.EmptyResultDataAccessException
import ru.cobalt42.auth.modal.role.Role

class RoleRepositoryImpl : RoleRepository {
    override suspend fun findByUid(uid: String): Role =
        try {
             MongoInit.roleCollection.findOne(Role::uid eq uid)!!
        } catch (e: Exception) {
            throw EmptyResultDataAccessException()
        }

    override suspend fun findAll(skip: Int, size: Int): List<Role> =
        MongoInit.roleCollection.find().skip(skip).limit(size).toList()

    override suspend fun getCountAll(): Long = MongoInit.roleCollection.countDocuments()

    override suspend fun findByNameContainingIgnoreCase(name: String, skip: Int, size: Int): List<Role> =
        MongoInit.roleCollection.find(Role::name regex name).skip(skip).limit(size).toList()

    override suspend fun getCountByName(name: String): Long =
        MongoInit.roleCollection.countDocuments("{ 'name': { \$regex: '$name', \$options: 'i'}}")

    override suspend fun deleteByUid(uid: String) {
        MongoInit.roleCollection.deleteOne(Role::uid eq uid)
    }

    override suspend fun save(data: Role) {
        MongoInit.roleCollection.save(data)
    }
}