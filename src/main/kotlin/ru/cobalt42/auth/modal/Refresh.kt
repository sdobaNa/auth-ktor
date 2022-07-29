package ru.cobalt42.auth.modal

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import ru.cobalt42.auth.modal.user.User

@Serializable
data class Refresh(
    var refresh: String = "",
    var exp: String = "",
    var token: String = "",
    var user: String = "",
    @Contextual
    var _id: Id<User> = ObjectId.get().toId()
)