package ru.cobalt42.auth.repository.user

import org.litote.kmongo.eq
import org.litote.kmongo.regex
import ru.cobalt42.auth.config.MongoInit
import ru.cobalt42.auth.exception.EmptyResultDataAccessException
import ru.cobalt42.auth.modal.user.User

class UserRepositoryImpl : UserRepository {
    override suspend fun findByUid(uid: String): User =
        try {
            MongoInit.userCollection.findOne(User::uid eq uid)!!
        } catch (e: Exception) {
            throw EmptyResultDataAccessException()
        }

    override suspend fun findByLogin(login: String): User =
        try {
            MongoInit.userCollection.findOne(User::login eq login)!!
        } catch (e: Exception) {
            throw EmptyResultDataAccessException()
        }

    override suspend fun findAll(skip: Int, size: Int): List<User> =
        MongoInit.userCollection.find().skip(skip).limit(size).toList()

    override suspend fun getCountAll(): Long = MongoInit.userCollection.countDocuments()

    override suspend fun findByLoginContainingIgnoreCase(login: String, skip: Int, size: Int): List<User> =
        MongoInit.userCollection.find(User::login regex login).skip(skip).limit(size).toList()

    override suspend fun getCountByName(name: String): Long =
        MongoInit.userCollection.countDocuments("{ 'name': { \$regex: '$name', \$options: 'i'}}")

    override suspend fun deleteByUid(uid: String) {
        MongoInit.userCollection.deleteOne(User::uid eq uid)
    }

    override suspend fun save(data: User) {
        MongoInit.userCollection.save(data)
    }
}