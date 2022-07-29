package ru.cobalt42.auth.config

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import ru.cobalt42.auth.modal.Refresh
import ru.cobalt42.auth.modal.role.Role
import ru.cobalt42.auth.modal.user.User
import ru.cobalt42.auth.mongoHost
import ru.cobalt42.auth.testDB

object MongoInit {
    var refreshCollection: CoroutineCollection<Refresh>
    var roleCollection: CoroutineCollection<Role>
    var userCollection: CoroutineCollection<User>

    val client = KMongo.createClient("mongodb://$mongoHost:27017").coroutine

    init {
        if(testDB.toBoolean()) {
            val database = client.getDatabase("testAuth")
            refreshCollection = database.getCollection("testRefresh")
            roleCollection = database.getCollection("testRole")
            userCollection = database.getCollection("testUser")
        } else {
            val database = client.getDatabase("auth")
            refreshCollection = database.getCollection()
            roleCollection = database.getCollection()
            userCollection = database.getCollection()
        }
    }
}