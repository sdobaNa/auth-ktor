package ru.cobalt42.auth.service

import ru.cobalt42.auth.dto.Authorization
import ru.cobalt42.auth.dto.DefaultResponse
import ru.cobalt42.auth.dto.RefreshData

interface AuthorizationService {
    suspend fun generate(authorization: Authorization, isAdminPanel: Boolean): DefaultResponse<RefreshData>
    suspend fun refresh(refreshData: RefreshData): DefaultResponse<RefreshData>
}