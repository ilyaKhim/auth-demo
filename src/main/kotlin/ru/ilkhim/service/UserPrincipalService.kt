package ru.ilkhim.service

import org.springframework.security.core.userdetails.UserDetailsService
import ru.ilkhim.model.UserPrincipal

interface UserPrincipalService : UserDetailsService {
    fun createUserPrincipal(userPrincipal: UserPrincipal): UserPrincipal

    override fun loadUserByUsername(email: String): UserPrincipal
}
