package ru.cobalt42.auth.modal.user

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId

@Serializable
data class User(
    var uid: String = "",
    var disabled: Boolean = false,
    var firstName: String = "",
    var secondName: String = "",
    var lastName: String = "",
    var name: String = "",
    var organization: String = "",
    var position: String = "",
    var phoneNumber: String = "",
    var mail: String = "",
    var login: String = "",
    var password: String = "",
    var avatar: Logo = Logo(),
    var roles: List<String> = emptyList(),
    @Contextual
    var _id: Id<User> = ObjectId.get().toId()
)
