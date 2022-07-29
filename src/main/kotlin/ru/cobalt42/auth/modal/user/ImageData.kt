package ru.cobalt42.auth.modal.user

import kotlinx.serialization.Serializable
import ru.cobalt42.auth.modal.dictionary.Target

@Serializable
data class ImageData(
    val fileData: String = "",
    val originName: String = "",
    val target: Target = Target(),
    val uname: String = ""
)