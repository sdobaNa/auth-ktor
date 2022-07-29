package ru.cobalt42.auth

import org.koin.dsl.module
import ru.cobalt42.auth.repository.custom.MainRepository
import ru.cobalt42.auth.repository.custom.MainRepositoryImpl
import ru.cobalt42.auth.repository.refresh.RefreshRepository
import ru.cobalt42.auth.repository.refresh.RefreshRepositoryImpl
import ru.cobalt42.auth.repository.role.RoleRepository
import ru.cobalt42.auth.repository.role.RoleRepositoryImpl
import ru.cobalt42.auth.repository.user.UserRepository
import ru.cobalt42.auth.repository.user.UserRepositoryImpl
import ru.cobalt42.auth.service.*
import ru.cobalt42.auth.util.SystemMessages

val fileAppModule = module(createdAtStart = true) {
    single<RefreshRepository> { RefreshRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
    single<RoleRepository> { RoleRepositoryImpl() }
    single<MainRepository> { MainRepositoryImpl() }
    single { SystemMessages(get()) }
    single<RoleService> { RoleServiceImpl(get(), get()) }
    single<UserService> { UserServiceImpl(get(), get(), get()) }
    single<AuthorizationService> { AuthorizationServiceImpl(get(), get(), get()) }
}