package ru.ilkhim.service

import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import org.mapstruct.MappingTarget
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.ilkhim.web.model.request.CreateUserRequestDto
import ru.ilkhim.model.Role
import ru.ilkhim.model.UserDto
import ru.ilkhim.model.UserPrincipal
import ru.ilkhim.persistence.model.User
import ru.ilkhim.persistence.model.UserRole

@Component
@Mapper(componentModel = SPRING)
abstract class UserMapper {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Mapping(target = "roles", ignore = true)
    @Mapping(source = "password", target = "userPassword")
    abstract fun toUserPrincipal(createUserRequestDto: CreateUserRequestDto): UserPrincipal

    @AfterMapping
    fun handleRoles(
        @MappingTarget userPrincipal: UserPrincipal,
        createUserRequestDto: CreateUserRequestDto,
    ) {
        userPrincipal.roles = setOf(Role.USER)
    }

    @Mapping(source = "roles", target = "roles", ignore = true)
    @Mapping(source = "password", target = "userPassword")
    abstract fun toUserPrincipal(user: User): UserPrincipal

    @AfterMapping
    fun handleRoles(
        @MappingTarget userPrincipal: UserPrincipal,
        user: User,
    ) {
        userPrincipal.roles = user.roles.map { it.role }.toSet()
    }

    @Mapping(source = "roles", target = "roles", ignore = true)
    @Mapping(source = "userPassword", target = "password", ignore = true)
    abstract fun toUser(userPrincipal: UserPrincipal): User

    @AfterMapping
    fun finishUser(
        @MappingTarget user: User,
        userPrincipal: UserPrincipal,
    ) {
        user.roles =
            userPrincipal.roles.map {
                UserRole(user, it)
            }.toSet()
        user.password = passwordEncoder.encode(userPrincipal.userPassword)
    }

    abstract fun toUserDto(user: User): UserDto
}
