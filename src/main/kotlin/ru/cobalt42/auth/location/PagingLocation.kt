package ru.cobalt42.auth.location

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/")
data class PagingLocation(val page: Int = 1, val size: Int = 16, val search: String = "")
