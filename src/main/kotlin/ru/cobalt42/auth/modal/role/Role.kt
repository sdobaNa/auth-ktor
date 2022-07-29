package ru.cobalt42.auth.modal.role

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import ru.cobalt42.auth.modal.user.User

@Serializable
data class Role(
    var uid: String = "",
    var name: String = "",
    var permissions: List<Permission> = emptyList(),
    @Contextual
    var _id: Id<User> = ObjectId.get().toId()
)
