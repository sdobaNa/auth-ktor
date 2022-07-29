package ru.cobalt42.auth.repository.custom

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.util.*
import ru.cobalt42.auth.modal.dictionary.ExceptionMessage
import ru.cobalt42.auth.mongoHost

class MainRepositoryImpl : MainRepository {

    @OptIn(InternalAPI::class)
    override suspend fun getMessages(authToken: String): Array<ExceptionMessage> {
        val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
            install(DefaultRequest) {
                headers.append("Authorization", authToken)
            }
        }
        val messages: Array<ExceptionMessage> = client.get("http://$mongoHost:3300/api/main/refs/systemMessages")
        client.close()
        return messages
    }

}